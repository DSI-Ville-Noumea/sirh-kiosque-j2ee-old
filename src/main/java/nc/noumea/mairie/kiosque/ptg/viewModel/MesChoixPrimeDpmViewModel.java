package nc.noumea.mairie.kiosque.ptg.viewModel;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.DpmIndemniteAnneeDto;
import nc.noumea.mairie.kiosque.ptg.dto.DpmIndemniteChoixAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.kiosque.viewModel.AbstractViewModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

public class MesChoixPrimeDpmViewModel extends AbstractViewModel {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8782343578282181898L;
	
	public final static String RADIO_BUTTON_RECUPERATION = "Recuperation";
	public final static String RADIO_BUTTON_INDEMNITE = "Indemnite";
	
	private DpmIndemniteChoixAgentDto choixCourant;
	private DpmIndemniteChoixAgentDto choixAnneeEnCours;
	
	private DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMMM yyyy");

	@Init
	public void initMesChoixPrimeDpm() {
		
		// on recherche les campagnes questions/choix ouvertes pour ce jour
		List<DpmIndemniteAnneeDto> listCampagnes = ptgWsConsumer.getListDpmIndemAnneeOuverte();
		
		if(null != listCampagnes
				&& !listCampagnes.isEmpty()) {
			setChoixCourant(ptgWsConsumer.getIndemniteChoixAgent(getCurrentUser().getAgent().getIdAgent(), listCampagnes.get(0).getAnnee()));
		}
		
		// si le choix courant correspond a l annee en cours
		// on ne fait rien
		// par contre, si le choix courant correspond a l annee prochaine
		// on affiche le choix de l annee en cours
		if(null == getChoixCourant()
				|| !getChoixCourant().getDpmIndemniteAnnee().getAnnee().equals(new DateTime().getYear())) {

			DpmIndemniteAnneeDto dpmAnneeEnCours = ptgWsConsumer.getDpmIndemAnneeEnCours();
			setChoixAnneeEnCours(ptgWsConsumer.getIndemniteChoixAgent(getCurrentUser().getAgent().getIdAgent(), dpmAnneeEnCours.getAnnee()));
		}
	}
	
	@Command
	public void enregistreChoixDpm() {
		
		getChoixCourant().setIdAgent(getCurrentUser().getAgent().getIdAgent());
		getChoixCourant().setIdAgentCreation(getCurrentUser().getAgent().getIdAgent());
		
		getChoixCourant().setChoixIndemnite(RADIO_BUTTON_INDEMNITE.equals(getChoixCourant().getRadioButtonZK()));
		getChoixCourant().setChoixRecuperation(RADIO_BUTTON_RECUPERATION.equals(getChoixCourant().getRadioButtonZK()));
		
		ReturnMessageDto result = ptgWsConsumer.saveIndemniteChoixAgent(getCurrentUser().getAgent().getIdAgent(), getChoixCourant());
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();

		for (String error : result.getErrors()) {
			ValidationMessage vm = new ValidationMessage(error);
			listErreur.add(vm);
		}
		for (String info : result.getInfos()) {
			ValidationMessage vm = new ValidationMessage(info);
			listInfo.add(vm);
		}
		map.put("errors", listErreur);
		map.put("infos", listInfo);
		Executions.createComponents("/messages/returnMessage.zul", null, map);
	}

	public String getTitreChoixCourant() {
		if(null != getChoixCourant() && null != getChoixCourant().getDpmIndemniteAnnee()) {
			return "Mon choix pour l'année "
					+ getChoixCourant().getDpmIndemniteAnnee().getAnnee();
		}
		return "";
	}

	public String getPhraseChoixCourant() {
		if(null != getChoixCourant() && null != getChoixCourant().getDpmIndemniteAnnee()) {
			return "Vous avez le choix entre la récupération de vos heures ou l’indemnité de travail : attention votre choix pour l'année "
					+ getChoixCourant().getDpmIndemniteAnnee().getAnnee() 
					+ " sera validé le " + new DateTime(getChoixCourant().getDpmIndemniteAnnee().getDateFin()).toString(dtf) 
					+ " sans retour possible.";
		}
		return "";
	}

	public String getTitreChoixAnneeEnCours() {
		if(null != getChoixAnneeEnCours() && null != getChoixAnneeEnCours().getDpmIndemniteAnnee()) {
			return "Mon choix pour l'année "
					+ getChoixAnneeEnCours().getDpmIndemniteAnnee().getAnnee();
		}
		return "";
	}
	
	public String getPhraseChoixAnneeEnCours() {
		if(null != getChoixAnneeEnCours()
				&& null != getChoixAnneeEnCours().getDpmIndemniteAnnee()) {
			StringBuffer buffer = new StringBuffer( "Pour l'année "); 
			buffer.append(getChoixAnneeEnCours().getDpmIndemniteAnnee().getAnnee()); 
			buffer.append(", vous avez choisi ");
			
			if(getChoixAnneeEnCours().isChoixIndemnite()) {
				buffer.append("l’indemnité de travail.");
			} else if(getChoixAnneeEnCours().isChoixRecuperation()) {
				buffer.append("la récupération de vos heures.");
			}else{
				buffer.append("NEANT.");
			}
			
			return buffer.toString();
		}
		return "";
	}

	public DpmIndemniteChoixAgentDto getChoixCourant() {
		return choixCourant;
	}

	public void setChoixCourant(DpmIndemniteChoixAgentDto choixCourant) {
		this.choixCourant = choixCourant;
	}

	public DpmIndemniteChoixAgentDto getChoixAnneeEnCours() {
		return choixAnneeEnCours;
	}

	public void setChoixAnneeEnCours(DpmIndemniteChoixAgentDto choixAnneeEnCours) {
		this.choixAnneeEnCours = choixAnneeEnCours;
	}
	
}
