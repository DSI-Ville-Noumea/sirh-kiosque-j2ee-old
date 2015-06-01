package nc.noumea.mairie.kiosque.abs.droits.viewModel;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutAgentOperateurViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<AgentDto> listeAgents;

	private List<AgentDto> listeAgentsExistants;

	private AgentDto operateur;

	private AgentDto approbateur;

	private String title;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initAgentOperateur(@ExecutionArgParam("agentsExistants") List<AgentDto> agentsExistants,
			@ExecutionArgParam("operateur") AgentDto operateur, @ExecutionArgParam("approbateur") AgentDto approbateur) {
		// on sauvegarde qui est l'approbateur
		setApprobateur(approbateur);
		// on sauvegarde qui est l'operateur
		setOperateur(operateur);
		// on sauvegarde les agnts dejà affectés à l'operateur
		setListeAgentsExistants(agentsExistants);
		// on charge tous les agents de l'approbateur
		List<AgentDto> result = absWsConsumer.getAgentsApprobateur(approbateur.getIdAgent());
		setListeAgents(transformeListe(result));
		setTailleListe("5");
		setTitle("Sélection des agents affectés à l'opérateur " + getOperateur().getNom() + " "
				+ getOperateur().getPrenom());
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void doCheckedAll(@BindingParam("ref") List<AgentDto> listDto, @BindingParam("box") Checkbox box) {
		for (AgentDto dto : getListeAgents()) {
			if (box.isChecked()) {
				dto.setSelectedDroitAbs(true);
				doChecked(dto);
			} else {
				dto.setSelectedDroitAbs(false);
				doChecked(dto);
			}
		}

	}

	@Command
	public void doChecked(@BindingParam("ref") AgentDto dto) {
		if (dto.isSelectedDroitAbs()) {
			if (!getListeAgentsExistants().contains(dto))
				getListeAgentsExistants().add(dto);
		} else {
			if (getListeAgentsExistants().contains(dto))
				getListeAgentsExistants().remove(dto);
		}

	}

	@Command
	public void saveAgent(@BindingParam("win") Window window) {
		ReturnMessageDto result = absWsConsumer.saveAgentsOperateur(getApprobateur().getIdAgent(),
				getOperateur().getIdAgent(), getListeAgentsExistants());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add(
					"Les agents affectés à l'opérateur " + getOperateur().getNom() + " " + getOperateur().getPrenom()
							+ " ont été enregistrés correctement.");
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
			window.detach();
		}

	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void doSearch() {
		List<AgentDto> list = new ArrayList<AgentDto>();
		if (getFilter() != null && !"".equals(getFilter()) && getListeAgents() != null) {
			for (AgentDto item : getListeAgents()) {
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
			List<AgentDto> result = absWsConsumer.getAgentsApprobateur(approbateur.getIdAgent());
			setListeAgents(transformeListe(result));
		}
	}

	private List<AgentDto> transformeListe(List<AgentDto> result) {
		List<AgentDto> listFinale = new ArrayList<AgentDto>();
		for (AgentDto agDto : result) {
			agDto.setSelectedDroitAbs(getListeAgentsExistants().contains(agDto));
			listFinale.add(agDto);
		}
		Collections.sort(listFinale);
		return listFinale;
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public List<AgentDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentDto> listeAgents) {
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

	public List<AgentDto> getListeAgentsExistants() {
		return listeAgentsExistants;
	}

	public void setListeAgentsExistants(List<AgentDto> listeAgentsExistants) {
		this.listeAgentsExistants = listeAgentsExistants;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public AgentDto getOperateur() {
		return operateur;
	}

	public void setOperateur(AgentDto operateur) {
		this.operateur = operateur;
	}

	public AgentDto getApprobateur() {
		return approbateur;
	}

	public void setApprobateur(AgentDto approbateur) {
		this.approbateur = approbateur;
	}
}
