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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.ads.dto.EntiteDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.TitreRepasDemandeDto;
import nc.noumea.mairie.kiosque.viewModel.AbstractViewModel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Tab;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GestionTitreRepasViewModel extends AbstractViewModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8405659633984953740L;

	private Tab tabCourant;
	private List<TitreRepasDemandeDto> listeTitreRepas;

	/* POUR LES FILTRES */
	private List<EntiteDto> listeServicesFiltre;
	private EntiteDto serviceFiltre;
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private List<RefEtatPointageDto> listeEtatTitreRepasFiltre;
	private RefEtatPointageDto etatTitreRepasFiltre;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initGestionTitreRepas() {
		// on recharge les états d'absences pour les filtres
		List<RefEtatPointageDto> filtreEtat = ptgWsConsumer.getEtatTitreRepasKiosque();
		setListeEtatTitreRepasFiltre(filtreEtat);
		setTailleListe("5");

		// on charge les service pour les filtres
		List<EntiteDto> filtreService = ptgWsConsumer.getServicesPointages(getCurrentUser().getAgent().getIdAgent());

		EntiteDto tousLesServices = new EntiteDto();
		tousLesServices.setSigle("TousLesServices");
		tousLesServices.setLabelCourt("Tous les services");
		List<EntiteDto> filtreAllService = new ArrayList<>();
		filtreAllService.add(tousLesServices);
		filtreAllService.addAll(filtreService);
		setListeServicesFiltre(filtreAllService);
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
		// filtrer(null);
	}

	@Command
	@NotifyChange({ "*" })
	public void filtrer() {
		List<TitreRepasDemandeDto> result = ptgWsConsumer.getListTitreRepas(getCurrentUser().getAgent().getIdAgent(), getDateDebutFiltre(), getDateFinFiltre(), getServiceFiltre() == null ? null
				: getServiceFiltre().getIdEntite(), getAgentFiltre() == null ? null : getAgentFiltre().getIdAgent(), getEtatTitreRepasFiltre() == null ? null : getEtatTitreRepasFiltre()
				.getIdRefEtat(), null);
		setListeTitreRepas(result);
	}

	@Command
	@NotifyChange({ "dateDebutFiltre", "serviceFiltre", "dateFinFiltre", "agentFiltre", "etatTitreRepasFiltre" })
	public void viderFiltre() {
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setServiceFiltre(null);
		setAgentFiltre(null);
		setEtatTitreRepasFiltre(null);
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void afficheListeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(getCurrentUser().getAgent().getIdAgent(), getServiceFiltre().getIdEntite());
		setListeAgentsFiltre(filtreAgent);
	}

	public List<TitreRepasDemandeDto> getHistoriqueTitreRepas(TitreRepasDemandeDto dto) {
		List<TitreRepasDemandeDto> result = ptgWsConsumer.getHistoriqueTitreRepas(dto.getIdTrDemande());
		return result;
	}

	public String concatAgent(AgentDto ag) {
		return ag.getNom() + " " + ag.getPrenom();
	}

	public String concatAgentNomatr(AgentDto ag) {
		if (ag == null || ag.getIdAgent() == null) {
			return "erreur";
		}
		String nomatr = ag.getIdAgent().toString().substring(3, ag.getIdAgent().toString().length());
		return ag.getNom() + " " + ag.getPrenom() + " (" + nomatr + ")";
	}

	public String dateSaisieToString(Date date) {
		SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
		return sdfJour.format(date) + " à " + sdfHeure.format(date);
	}

	public String etatToString(Integer idRefEtat) {
		return EtatPointageEnum.getEtatPointageEnum(idRefEtat).getLibEtat();
	}

	public String booleanToString(boolean commande) {
		return commande ? "oui" : "non";
	}

	@Command
	@NotifyChange({ "listeDemandes", "listeEtatAbsenceFiltre", "listeEtatsSelectionnes" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		// setListeDemandes(null);
		// // on recharge les états d'absences pour les filtres
		// List<RefEtatAbsenceDto> filtreEtat =
		// absWsConsumer.getEtatAbsenceKiosque(tab.getId());
		// setListeEtatAbsenceFiltre(filtreEtat);
		// on sauvegarde l'onglet
		setTabCourant(tab);
		// setListeEtatsSelectionnes(null);
		// filtrer(null);
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
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

	public RefEtatPointageDto getEtatTitreRepasFiltre() {
		return etatTitreRepasFiltre;
	}

	public void setEtatTitreRepasFiltre(RefEtatPointageDto etatTitreRepasFiltre) {
		this.etatTitreRepasFiltre = etatTitreRepasFiltre;
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

	public List<EntiteDto> getListeServicesFiltre() {
		return listeServicesFiltre;
	}

	public void setListeServicesFiltre(List<EntiteDto> listeServicesFiltre) {
		this.listeServicesFiltre = listeServicesFiltre;
	}

	public EntiteDto getServiceFiltre() {
		return serviceFiltre;
	}

	public void setServiceFiltre(EntiteDto serviceFiltre) {
		this.serviceFiltre = serviceFiltre;
	}

	public List<AgentDto> getListeAgentsFiltre() {
		return listeAgentsFiltre;
	}

	public void setListeAgentsFiltre(List<AgentDto> listeAgentsFiltre) {
		this.listeAgentsFiltre = listeAgentsFiltre;
	}

	public AgentDto getAgentFiltre() {
		return agentFiltre;
	}

	public void setAgentFiltre(AgentDto agentFiltre) {
		this.agentFiltre = agentFiltre;
	}
}
