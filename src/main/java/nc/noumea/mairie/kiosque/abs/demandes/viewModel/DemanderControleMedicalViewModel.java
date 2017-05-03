package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.BindUtils;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2017 Mairie de Nouméa
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

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import flexjson.JSONSerializer;
import nc.noumea.mairie.kiosque.abs.dto.ControleMedicalDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.transformer.MSDateTransformer;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

@VariableResolver(DelegatingVariableResolver.class)
public class DemanderControleMedicalViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;
	
	private DemandeDto demandeAbsenceCourant;
	private ControleMedicalDto demandeControleMedicalCourant;

	private ProfilAgentDto currentUser;
	private String tabTitle;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("demandeCourant") DemandeDto demande) {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupere la demande selectionnée
		setDemandeAbsenceCourant(demande);
		
		ControleMedicalDto controleMedical = absWsConsumer.getControleMedicalByDemande(demande.getIdDemande());

		// TODO : Remove after tests
		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class).deepSerialize(controleMedical);
		
		if (controleMedical == null)
			controleMedical = new ControleMedicalDto();
		
		setDemandeControleMedicalCourant(controleMedical);
		setTabTile(controleMedical.getId() != null ? "Visualisation d'une demande de contrôle médical" : "Création d'une demande de contrôle médical");
	}

	@Command
	public void approuveDemande(@BindingParam("win") Window window) {
		
		// TODO : Suppr. les System.out.println()
		if (currentUser == null || currentUser.getAgent() == null) {
			System.out.println("Aucun agent n'est connecté");
			return;
		}
		// TODO : Suppr. les System.out.println()
		if (demandeControleMedicalCourant.getId() != null) {
			System.out.println("Vous ne pouvez modifier une demande de contrôle médical existante.");
			return;
		}
		
		if (IsFormValid() && getDemandeAbsenceCourant() != null) {

			ControleMedicalDto demandeControle = new ControleMedicalDto();
			demandeControle.setDate(new Date());
			demandeControle.setDemandeMaladie(demandeAbsenceCourant);
			demandeControle.setIdAgent(currentUser.getAgent().getIdAgent());
			demandeControle.setCommentaire(demandeControleMedicalCourant.getCommentaire());

			ReturnMessageDto result = absWsConsumer.persistControleMedical(demandeControle);

			if (result.getErrors().size() > 0 || result.getInfos().size() > 0) {
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
				if (listErreur.size() == 0) {
					BindUtils.postGlobalCommand(null, null, "refreshListeDemande", null);
					window.detach();
				}
			}
		}
	}

	private boolean IsFormValid() {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Le commentaire est obligatoire
		if (getDemandeControleMedicalCourant() == null || getDemandeControleMedicalCourant().getCommentaire() == null) {
			vList.add(new ValidationMessage("Le commentaire est obligatoire."));
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}
	
	public boolean readOnly() {
		return !(getDemandeControleMedicalCourant() == null || getDemandeControleMedicalCourant().getId() == null);
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public DemandeDto getDemandeAbsenceCourant() {
		return demandeAbsenceCourant;
	}

	public void setDemandeAbsenceCourant(DemandeDto demandeAbsenceCourant) {
		this.demandeAbsenceCourant = demandeAbsenceCourant;
	}

	public ControleMedicalDto getDemandeControleMedicalCourant() {
		return demandeControleMedicalCourant;
	}

	public void setDemandeControleMedicalCourant(ControleMedicalDto demandeControleMedicalCourant) {
		this.demandeControleMedicalCourant = demandeControleMedicalCourant;
	}

	public String getTabTitle() {
		return tabTitle;
	}

	public void setTabTile(String tabTitle) {
		this.tabTitle = tabTitle;
	}
	
}
