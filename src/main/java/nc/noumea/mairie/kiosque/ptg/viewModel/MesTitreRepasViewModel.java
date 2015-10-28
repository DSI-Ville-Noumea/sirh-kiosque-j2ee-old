package nc.noumea.mairie.kiosque.ptg.viewModel;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.EtatTitreRepasDemandeDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.TitreRepasDemandeDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.kiosque.viewModel.AbstractViewModel;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MesTitreRepasViewModel extends AbstractViewModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6673727695582022385L;

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private List<TitreRepasDemandeDto> listeTitreRepas;
	private TitreRepasDemandeDto titreRepasCourant;

	private String checkTitreRepas;

	/* POUR LES FILTRES */
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private List<RefEtatPointageDto> listeEtatTitreRepasFiltre;
	private RefEtatPointageDto etatTitreRepasFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initMesTitreRepas() {
		// on recharge les états d'absences pour les filtres
		List<RefEtatPointageDto> filtreEtat = ptgWsConsumer.getEtatTitreRepasKiosque();
		setListeEtatTitreRepasFiltre(filtreEtat);
		setTailleListe("5");
		// on recupere le titre repas deja saisi
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		List<TitreRepasDemandeDto> titreRepasCourant = ptgWsConsumer.getListTitreRepas(getCurrentUser().getAgent().getIdAgent(), null, null, null, getCurrentUser().getAgent().getIdAgent(), null,
				cal.getTime());
		if (titreRepasCourant == null || titreRepasCourant.size() != 1) {
			setTitreRepasCourant(null);
		} else {
			setTitreRepasCourant(titreRepasCourant.get(0));
		}
		setCheckTitreRepas(getTitreRepasCourant() == null ? "non" : getTitreRepasCourant().getCommande() ? "oui" : "non");
	}

	@Command
	@NotifyChange({ "*" })
	public void engistreTitreRepas() throws ParseException {
		if (getTitreRepasCourant() == null) {
			setTitreRepasCourant(new TitreRepasDemandeDto());
		}
		getTitreRepasCourant().setIdAgent(getCurrentUser().getAgent().getIdAgent());
		getTitreRepasCourant().setCommande(getCheckTitreRepas().equals("oui") ? true : false);
		getTitreRepasCourant().setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());

		ReturnMessageDto result = ptgWsConsumer.setTitreRepas(getCurrentUser().getAgent().getIdAgent(), Arrays.asList(getTitreRepasCourant()));

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

	@Command
	@NotifyChange({ "listeTitreRepas" })
	public void doSearch() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<TitreRepasDemandeDto> list = new ArrayList<TitreRepasDemandeDto>();
		if (getFilter() != null && !"".equals(getFilter()) && getListeTitreRepas() != null) {
			for (TitreRepasDemandeDto item : getListeTitreRepas()) {
				if (sdf.format(item.getDateMonth()).contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setListeTitreRepas(list);
		} else {
			filtrer();
		}
	}

	public String getPhraseTitreRepas() {
		return "Voulez vous commander les tickets repas pour le mois " + getMonth(new DateTime().getMonthOfYear()) + " : ";
	}

	private String getMonth(int monthOfYear) {
		String monthString = null;
		switch (monthOfYear) {
			case 1:
				monthString = "de janvier";
				break;
			case 2:
				monthString = "de février";
				break;
			case 3:
				monthString = "de mars";
				break;
			case 4:
				monthString = "d'avril";
				break;
			case 5:
				monthString = "de mai";
				break;
			case 6:
				monthString = "de juin";
				break;
			case 7:
				monthString = "de juillet";
				break;
			case 8:
				monthString = "d'aôut";
				break;
			case 9:
				monthString = "de septembre";
				break;
			case 10:
				monthString = "d'octobre";
				break;
			case 11:
				monthString = "de novembre";
				break;
			case 12:
				monthString = "de décembre";
				break;
		}
		return monthString;
	}

	public List<EtatTitreRepasDemandeDto> getHistoriqueTitreRepas(TitreRepasDemandeDto dto) {
		return dto.getListEtats();
	}

	@Command
	@NotifyChange({ "*" })
	public void filtrer() {
		List<TitreRepasDemandeDto> result = ptgWsConsumer.getListTitreRepas(getCurrentUser().getAgent().getIdAgent(), getDateDebutFiltre(), getDateFinFiltre(), null, getCurrentUser().getAgent()
				.getIdAgent(), getEtatTitreRepasFiltre() == null ? null : getEtatTitreRepasFiltre().getIdRefEtat(), null);
		setListeTitreRepas(result);
	}

	@Command
	@NotifyChange({ "dateDebutFiltre", "dateFinFiltre", "etatTitreRepasFiltre" })
	public void viderFiltre() {
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setEtatTitreRepasFiltre(null);
	}

	public String dateSaisieToString(Date date) {
		SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
		return sdfJour.format(date) + " à " + sdfHeure.format(date);
	}

	public String dateEtatToString(TitreRepasDemandeDto titreRepas) {
		SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
		return sdfJour.format(titreRepas.getListEtats().get(0).getDateMaj()) + " à " + sdfHeure.format(titreRepas.getListEtats().get(0).getDateMaj());
	}

	public String etatToString(Integer idRefEtat) {
		return EtatPointageEnum.getEtatPointageEnum(idRefEtat).getLibEtat();
	}

	public String concatAgentNomatr(AgentDto ag) {
		String nomatr = ag.getIdAgent().toString().substring(3, ag.getIdAgent().toString().length());
		return ag.getNom() + " " + ag.getPrenom() + " (" + nomatr + ")";
	}

	public Date getDateDebutFiltre() {
		return dateDebutFiltre;
	}

	public void setDateDebutFiltre(Date dateDebutFiltre) {
		this.dateDebutFiltre = dateDebutFiltre;
	}

	public Date getDateFinFiltre() {
		return dateFinFiltre;
	}

	public void setDateFinFiltre(Date dateFinFiltre) {
		this.dateFinFiltre = dateFinFiltre;
	}

	public List<RefEtatPointageDto> getListeEtatTitreRepasFiltre() {
		return listeEtatTitreRepasFiltre;
	}

	public void setListeEtatTitreRepasFiltre(List<RefEtatPointageDto> listeEtatTitreRepasFiltre) {
		this.listeEtatTitreRepasFiltre = listeEtatTitreRepasFiltre;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getTailleListe() {
		return tailleListe;
	}

	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
	}

	public List<TitreRepasDemandeDto> getListeTitreRepas() {
		return listeTitreRepas;
	}

	public void setListeTitreRepas(List<TitreRepasDemandeDto> listeTitreRepas) {
		this.listeTitreRepas = listeTitreRepas;
	}

	public RefEtatPointageDto getEtatTitreRepasFiltre() {
		return etatTitreRepasFiltre;
	}

	public void setEtatTitreRepasFiltre(RefEtatPointageDto etatTitreRepasFiltre) {
		this.etatTitreRepasFiltre = etatTitreRepasFiltre;
	}

	public String getCheckTitreRepas() {
		return checkTitreRepas;
	}

	public void setCheckTitreRepas(String checkTitreRepas) {
		this.checkTitreRepas = checkTitreRepas;
	}

	public TitreRepasDemandeDto getTitreRepasCourant() {
		return titreRepasCourant;
	}

	public void setTitreRepasCourant(TitreRepasDemandeDto titreRepasCourant) {
		this.titreRepasCourant = titreRepasCourant;
	}
}
