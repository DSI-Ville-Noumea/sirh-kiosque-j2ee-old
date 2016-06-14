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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.ads.dto.EntiteDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.DpmIndemniteAnneeDto;
import nc.noumea.mairie.kiosque.ptg.dto.DpmIndemniteChoixAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.kiosque.viewModel.AbstractViewModel;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Tab;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GestionChoixPrimeDpmViewModel extends AbstractViewModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8405659633984953740L;

	private Tab tabCourant;

	private DpmIndemniteAnneeDto campagneDpmAnneeOuverte;
	private List<DpmIndemniteChoixAgentDto> listChoixSaisie;
	private List<DpmIndemniteChoixAgentDto> listChoixAnneeEnCours;
	private List<DpmIndemniteChoixAgentDto> listChoixAnneeEnCoursInitial;

	/* POUR LES FILTRES */
	private List<EntiteDto> listeServicesFiltre;
	private EntiteDto serviceFiltre;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initGestionChoixPrimeDpm() {
		
		setTailleListe("20");

		// on charge les services pour les filtres
		List<EntiteDto> filtreService = ptgWsConsumer.getServicesWithPrimeDpmPointages(getCurrentUser().getAgent().getIdAgent());

		EntiteDto tousLesServices = new EntiteDto();
		tousLesServices.setSigle("TousLesServices");
		tousLesServices.setLabelCourt("Tous les services");
		tousLesServices.setLabel("Tous les services");
		
		List<EntiteDto> filtreAllService = new ArrayList<>();
		filtreAllService.add(tousLesServices);
		filtreAllService.addAll(filtreService);
		
		setListeServicesFiltre(filtreAllService);
		
		// on charge si une campagne DPM est ouverte 
		List<DpmIndemniteAnneeDto> listCampagnes = ptgWsConsumer.getListDpmIndemAnneeOuverte();
		if(null != listCampagnes
				&& !listCampagnes.isEmpty()) {
			setCampagneDpmAnneeOuverte(listCampagnes.get(0));
		}
		
		List<DpmIndemniteChoixAgentDto> results = ptgWsConsumer.getListDpmIndemniteChoixAgent(
				getCurrentUser().getAgent().getIdAgent(), 
				new DateTime().getYear(), 
				null != getServiceFiltre() ? getServiceFiltre().getIdEntite() : null,
				null != getAgentFiltre() ? getAgentFiltre().getIdAgent() : null);
		
		// ce que on affiche
		setListChoixAnneeEnCours(results);
		// ce qu on sauvegarde pour la recherche instantanée
		setListChoixAnneeEnCoursInitial(results);
	}

	@Command
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
	}

	@Command
	@NotifyChange({ "listChoixSaisie", "listeAgentsFiltre" })
	public void chargeTabSaisie() {
		
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointagesWithPrimeDpm(getCurrentUser().getAgent().getIdAgent(), 
				null != getServiceFiltre() ? getServiceFiltre().getIdEntite() : null);
		setListeAgentsFiltre(filtreAgent);

		// on recupere les demandes deja saisies
		if(null != getCampagneDpmAnneeOuverte()) {
			List<DpmIndemniteChoixAgentDto> resultDejaSaisie = ptgWsConsumer.getListDpmIndemniteChoixAgent(
					getCurrentUser().getAgent().getIdAgent(), 
					getCampagneDpmAnneeOuverte().getAnnee(), 
					null != getServiceFiltre() ? getServiceFiltre().getIdEntite() : null,
					null != getAgentFiltre() ? getAgentFiltre().getIdAgent() : null);
			
			List<AgentDto> listExist = new ArrayList<AgentDto>();
			for (DpmIndemniteChoixAgentDto dto : resultDejaSaisie) {
				listExist.add(dto.getAgent());
			}
			
			List<DpmIndemniteChoixAgentDto> result = new ArrayList<DpmIndemniteChoixAgentDto>();
			result.addAll(resultDejaSaisie);
			
			for (AgentDto ag : getListeAgentsFiltre()) {
				if (!listExist.contains(ag)) {
					DpmIndemniteChoixAgentDto dtoSaisie = new DpmIndemniteChoixAgentDto();
					dtoSaisie.setAgent(new AgentWithServiceDto(ag, null, null));
					dtoSaisie.setIdAgent(ag.getIdAgent());
					result.add(dtoSaisie);
				}
			}
			setListChoixSaisie(result);
		}
	}

	@Command
	@NotifyChange({ "listChoixSaisie" })
	public void saveListChoixSaisie() {
		
		ReturnMessageDto result = ptgWsConsumer.saveListIndemniteChoixAgentForOperator(
				getCurrentUser().getAgent().getIdAgent(), getCampagneDpmAnneeOuverte().getAnnee(), 
				getListChoixSaisie());
		
		// on recharge les infos
		chargeTabSaisie();

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
	@NotifyChange({ "*" })
	public void filtrer() {
		
		List<DpmIndemniteChoixAgentDto> results = ptgWsConsumer.getListDpmIndemniteChoixAgent(
				getCurrentUser().getAgent().getIdAgent(), 
				new DateTime().getYear(), 
				null != getServiceFiltre() ? getServiceFiltre().getIdEntite() : null,
				null != getAgentFiltre() ? getAgentFiltre().getIdAgent() : null);
		
		// ce que on affiche
		setListChoixAnneeEnCours(results);
		// ce qu on sauvegarde pour la recherche instantanée
		setListChoixAnneeEnCoursInitial(results);
	}

	@Command
	@NotifyChange({ "listChoixAnneeEnCours", "listChoixSaisie" })
	public void doSearch() {
		
		List<DpmIndemniteChoixAgentDto> list = new ArrayList<DpmIndemniteChoixAgentDto>();
		
		if (getFilter() != null && !"".equals(getFilter()) && getListChoixAnneeEnCoursInitial() != null) {
			for (DpmIndemniteChoixAgentDto item : getListChoixAnneeEnCoursInitial()) {
				if (item.getAgent().getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgent().getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setListChoixAnneeEnCours(list);
		} else {
			filtrer();
		}
	}
	
	public String getPhraseSaisieChoixPrimeDpm() {
		if(null != getCampagneDpmAnneeOuverte()) {
			return "Saisie des choix agent concernant la prime DPM SDJF pour l'année " + getCampagneDpmAnneeOuverte().getAnnee();
		}
		return "Actuellement, il n'y a aucun choix à saisir.";
	}

	public String getPhraseChoixPrimeDpmAnneeEnCours() {
		return "Visualisation des choix agent concernant la prime DPM SDJF pour l'année " + new DateTime().getYear();
	}
	
	@Command
	@NotifyChange({ "serviceFiltre", "agentFiltre", "listChoixSaisie", "listeAgentsFiltre", "listeServicesFiltre" })
	public void viderFiltre() {
		setServiceFiltre(null);
		setAgentFiltre(null);
		setListeAgentsFiltre(null);
		setListChoixSaisie(null);
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void afficheListeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointagesWithPrimeDpm(getCurrentUser().getAgent().getIdAgent(), getServiceFiltre().getIdEntite());
		setListeAgentsFiltre(filtreAgent);
	}

	public String concatAgent(AgentDto ag) {
		return ag == null ? "" : ag.getNom() + " " + ag.getPrenom();
	}

	public String concatAgentNomatr(AgentDto ag) {
		String nomatr = ag.getIdAgent().toString().substring(3, ag.getIdAgent().toString().length());
		return ag.getNom() + " " + ag.getPrenom() + " (" + nomatr + ")";
	}

	@Command
	@NotifyChange({ "*" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		viderFiltre();
		// on sauvegarde l'onglet
		setTabCourant(tab);
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
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

	public List<DpmIndemniteChoixAgentDto> getListChoixSaisie() {
		return listChoixSaisie;
	}

	public void setListChoixSaisie(List<DpmIndemniteChoixAgentDto> listChoixSaisie) {
		this.listChoixSaisie = listChoixSaisie;
	}

	public List<DpmIndemniteChoixAgentDto> getListChoixAnneeEnCours() {
		return listChoixAnneeEnCours;
	}

	public void setListChoixAnneeEnCours(List<DpmIndemniteChoixAgentDto> listChoixAnneeEnCours) {
		this.listChoixAnneeEnCours = listChoixAnneeEnCours;
	}

	public DpmIndemniteAnneeDto getCampagneDpmAnneeOuverte() {
		return campagneDpmAnneeOuverte;
	}

	public void setCampagneDpmAnneeOuverte(DpmIndemniteAnneeDto campagneDpmAnneeOuverte) {
		this.campagneDpmAnneeOuverte = campagneDpmAnneeOuverte;
	}

	public List<DpmIndemniteChoixAgentDto> getListChoixAnneeEnCoursInitial() {
		return listChoixAnneeEnCoursInitial;
	}

	public void setListChoixAnneeEnCoursInitial(List<DpmIndemniteChoixAgentDto> listChoixAnneeEnCoursInitial) {
		this.listChoixAnneeEnCoursInitial = listChoixAnneeEnCoursInitial;
	}
	
}
