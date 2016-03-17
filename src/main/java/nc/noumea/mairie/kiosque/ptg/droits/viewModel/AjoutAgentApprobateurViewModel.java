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
import nc.noumea.mairie.kiosque.dto.EntiteWithAgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.viewModel.ServiceTreeModel;
import nc.noumea.mairie.kiosque.travail.viewModel.ServiceTreeNode;
import nc.noumea.mairie.kiosque.tree.utils.AbstractTreeUtils;
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
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutAgentApprobateurViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	// liste agents utilisee pour la recherche instantanee
	private List<AgentDto> listeAgents;

	// liste agents que l on sauvegarde dans PTG
	private List<AgentDto> listeAgentsExistants;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private ProfilAgentDto currentUser;

	// pour l'arbre des services
	private EntiteWithAgentWithServiceDto arbreService;
	private TreeModel<ServiceTreeNode> arbre;

	@Init
	public void initAjoutAgentApprobateur(@ExecutionArgParam("agentsExistants") List<AgentDto> agentsExistants) {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on sauvegarde qui sont les agents deja approuvés pour les coches
		setListeAgentsExistants(agentsExistants);
		// on vide
		viderZones();
		setTailleListe("10");

		EntiteWithAgentWithServiceDto tree = sirhWsConsumer.getListeEntiteWithAgentWithServiceDtoByIdServiceAds(
				currentUser.getIdServiceAds(), currentUser.getAgent().getIdAgent(), agentsExistants);
		setArbreService(tree);
		ServiceTreeModel serviceTreeModel = new ServiceTreeModel(getServiceTreeRoot(null));
		serviceTreeModel.setMultiple(true);
		setArbre(serviceTreeModel);
		// on coche le service si tous les agents de celui-ci sont coches
		AbstractTreeUtils.updateServiceTreeNode(getArbre().getRoot(), null, false, getListeAgentsExistants());
	}

	@Command
	public void saveAgent(@BindingParam("win") Window window) {
		ReturnMessageDto result = ptgWsConsumer.saveApprovedAgents(currentUser.getAgent().getIdAgent(), getListeAgentsExistants());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Les agents à approuver ont été enregistrés correctement.");
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
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	@Command
	@NotifyChange({ "arbre" })
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
			ServiceTreeModel serviceTreeModel = new ServiceTreeModel(getServiceTreeRoot(list));
			serviceTreeModel.setMultiple(true);
			serviceTreeModel.setOpenObjects(AbstractTreeUtils.getOpenObject(serviceTreeModel.getRoot()));
			setArbre(serviceTreeModel);
		} else {
			// on charge les sous agents
			ServiceTreeModel serviceTreeModel = new ServiceTreeModel(getServiceTreeRoot(null));
			serviceTreeModel.setMultiple(true);
			setArbre(serviceTreeModel);
		}
	}

	@Command
	@NotifyChange({ "arbre" })
	public void openAll() {
		ServiceTreeModel serviceTreeModel = new ServiceTreeModel(getServiceTreeRoot(null));
		serviceTreeModel.setMultiple(true);
		serviceTreeModel.setOpenObjects(AbstractTreeUtils.getOpenObject(serviceTreeModel.getRoot()));
		setArbre(serviceTreeModel);
		// on coche le service si tous les agents de celui-ci sont coches
		AbstractTreeUtils.updateServiceTreeNode(getArbre().getRoot(), null, false, getListeAgentsExistants());
	}

	@Command
	@NotifyChange({ "arbre" })
	public void closeAll() {
		ServiceTreeModel serviceTreeModel = new ServiceTreeModel(getServiceTreeRoot(null));
		serviceTreeModel.setMultiple(true);
		setArbre(serviceTreeModel);
		// on coche le service si tous les agents de celui-ci sont coches
		AbstractTreeUtils.updateServiceTreeNode(getArbre().getRoot(), null, false, getListeAgentsExistants());
	}

	@Command
	@NotifyChange({ "itemSelectedSet", "arbre" })
	public void selectNoeudArbre(@BindingParam("ref") ServiceTreeNode node) {

		if (null != node) {
			if (!AbstractTreeUtils.isInteger(node.getId())) {
				if (node.isSelectedDroitAbs()) {
					// on coche un service
					AbstractTreeUtils.updateServiceTreeNode(getArbre().getRoot(), node, true, getListeAgentsExistants());
				} else {
					// on decoche les agents du service
					AbstractTreeUtils.updateServiceTreeNode(getArbre().getRoot(), node, false, getListeAgentsExistants());
				}
			}
		}

		// si on decoche un agent
		// on decoche le service s il est coche
		AbstractTreeUtils.updateServiceTreeNode(getArbre().getRoot(), null, false, getListeAgentsExistants());

		if (AbstractTreeUtils.isInteger(node.getId())) {
			if (node.isSelectedDroitAbs()) {
				AgentDto ag = new AgentDto();
				ag.setIdAgent(new Integer(node.getId()));
				if (!getListeAgentsExistants().contains(ag)) {
					getListeAgentsExistants().add(ag);
				}
			} else {
				AgentDto ag = new AgentDto();
				ag.setIdAgent(new Integer(node.getId()));
				if (getListeAgentsExistants().contains(ag))
					getListeAgentsExistants().remove(ag);
			}
		}
	}

	// create a FooNodes tree structure and return the root
	private ServiceTreeNode getServiceTreeRoot(List<AgentDto> filtreAgent) {
		ServiceTreeNode root = new ServiceTreeNode(null, "", null);

		ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, getArbreService().getSigle(), getArbreService().getSigle());
		firstLevelNode.setClassCss("treeNodeService");
		firstLevelNode.setClassCssText("treeNodeServiceText");

		if (null != getArbreService().getListAgentWithServiceDto()) {
			for (AgentWithServiceDto agent : getArbreService().getListAgentWithServiceDto()) {
				if (null == filtreAgent || (filtreAgent.contains(agent))) {
					ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode, AbstractTreeUtils.concatAgentSansCivilite(agent), agent.getIdAgent().toString());
					getListeAgents().add(agent);
					firstLevelNode.appendChild(agentLevelNode);

					if (getListeAgentsExistants().contains(agent)) {
						agentLevelNode.setSelectedDroitAbs(true);
					}
				}
			}
		}
		root.appendChild(firstLevelNode);

		addServiceTreeNodeFromThreeRecursive(root.getChildren().get(0), getArbreService(), filtreAgent);

		return root;
	}

	private void addServiceTreeNodeFromThreeRecursive(ServiceTreeNode root, EntiteWithAgentWithServiceDto entite, List<AgentDto> filtreAgent) {

		if (null != entite && null != entite.getEntiteEnfantWithAgents()) {
			for (EntiteWithAgentWithServiceDto entiteEnfant : entite.getEntiteEnfantWithAgents()) {
				ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, entiteEnfant.getSigle(), entiteEnfant.getSigle());
				firstLevelNode.setClassCss("treeNodeService");
				firstLevelNode.setClassCssText("treeNodeServiceText");

				if (null != entiteEnfant.getListAgentWithServiceDto()) {
					for (AgentWithServiceDto agent : entiteEnfant.getListAgentWithServiceDto()) {
						if (null == filtreAgent || (filtreAgent.contains(agent))) {
							ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode, AbstractTreeUtils.concatAgentSansCivilite(agent), agent.getIdAgent().toString());
							getListeAgents().add(agent);
							firstLevelNode.appendChild(agentLevelNode);

							if (getListeAgentsExistants().contains(agent)) {
								agentLevelNode.setSelectedDroitAbs(true);
							}
						}
					}
				}
				root.appendChild(firstLevelNode);
				addServiceTreeNodeFromThreeRecursive(firstLevelNode, entiteEnfant, filtreAgent);
			}
		}
	}

	private void viderZones() {
		setListeAgents(null);
	}

	public List<AgentDto> getListeAgents() {
		if (null == listeAgents) {
			listeAgents = new ArrayList<AgentDto>();
		}
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

	public EntiteWithAgentWithServiceDto getArbreService() {
		return arbreService;
	}

	public void setArbreService(EntiteWithAgentWithServiceDto arbreService) {
		this.arbreService = arbreService;
	}

	public TreeModel<ServiceTreeNode> getArbre() {
		return arbre;
	}

	public void setArbre(TreeModel<ServiceTreeNode> arbre) {
		this.arbre = arbre;
	}

}
