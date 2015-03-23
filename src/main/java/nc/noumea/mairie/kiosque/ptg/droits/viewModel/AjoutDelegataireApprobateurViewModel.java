package nc.noumea.mairie.kiosque.ptg.droits.viewModel;

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

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutDelegataireApprobateurViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<AgentWithServiceDto> listeAgents;

	private List<AgentDto> listeOperateursExistants;

	private List<AgentDto> listeDelegataireExistants;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private ProfilAgentDto currentUser;

	@Init
	public void initAjoutDelegataire(@ExecutionArgParam("operateursExistants") List<AgentDto> operateursExistants,
			@ExecutionArgParam("delegataireExistants") AgentDto delegataireExistants) {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on sauvegarde qui sont les opérateurs de l'approbateur
		setListeOperateursExistants(operateursExistants);
		// on sauvegarde qui est le delegataire de l'approbateur
		List<AgentDto> delegataire = null;
		if (delegataireExistants != null) {
			delegataire = new ArrayList<>();
			delegataire.add(delegataireExistants);
		}
		setListeDelegataireExistants(delegataire);
		// on vide
		viderZones();
		// on charge tous les agents de la mairie
		List<AgentWithServiceDto> result = sirhWsConsumer.getListeAgentsMairie(currentUser.getAgent().getIdAgent());
		setListeAgents(result);
		setTailleListe("10");
	}

	@Command
	public void saveAgent(@BindingParam("win") Window window, @BindingParam("listBox") Listbox listAgent) {
		// on récupère les agents selectionnés
		List<Listitem> t = listAgent.getItems();
		AgentDto listSelect = new AgentDto();
		for (Listitem a : t) {
			if (a.isSelected())
				listSelect = (AgentDto) a.getValue();
		}
		DelegatorAndOperatorsDto dto = new DelegatorAndOperatorsDto();
		dto.setSaisisseurs(getListeOperateursExistants());
		dto.setDelegataire(listSelect);
		ReturnMessageDto result = ptgWsConsumer.saveDelegateAndOperator(currentUser.getAgent().getIdAgent(), dto);

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Le délégataire a été enregistré correctement.");
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
			BindUtils.postGlobalCommand(null, null, "refreshListeAgent", null);
			window.detach();
		}

	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void doSearch() {
		List<AgentWithServiceDto> list = new ArrayList<AgentWithServiceDto>();
		if (getFilter() != null && !"".equals(getFilter()) && getListeAgents() != null) {
			for (AgentWithServiceDto item : getListeAgents()) {
				if (item.getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setListeAgents(list);
		} else {
			List<AgentWithServiceDto> result = sirhWsConsumer.getListeAgentsMairie(currentUser.getAgent().getIdAgent());
			setListeAgents(result);
		}
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	private void viderZones() {
		setListeAgents(null);
	}

	public List<AgentWithServiceDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentWithServiceDto> listeAgents) {
		this.listeAgents = listeAgents;
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

	public List<AgentDto> getListeDelegataireExistants() {
		return listeDelegataireExistants;
	}

	public void setListeDelegataireExistants(List<AgentDto> listeDelegataireExistants) {
		this.listeDelegataireExistants = listeDelegataireExistants;
	}

	public List<AgentDto> getListeOperateursExistants() {
		return listeOperateursExistants;
	}

	public void setListeOperateursExistants(List<AgentDto> listeOperateursExistants) {
		this.listeOperateursExistants = listeOperateursExistants;
	}
}
