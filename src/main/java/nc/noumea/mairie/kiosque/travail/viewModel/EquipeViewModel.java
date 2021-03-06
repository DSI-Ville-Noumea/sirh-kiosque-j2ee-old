package nc.noumea.mairie.kiosque.travail.viewModel;

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
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.ext.Openable;

import nc.noumea.mairie.ads.dto.EntiteDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFinalisationDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.EstChefDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.ws.AlfrescoCMISService;
import nc.noumea.mairie.ws.IAdsWSConsumer;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EquipeViewModel extends SelectorComposer<Component> {

	private static final long			serialVersionUID	= 1L;

	@WireVariable
	private ISirhWSConsumer				sirhWsConsumer;

	@WireVariable
	private ISirhEaeWSConsumer			eaeWsConsumer;

	@WireVariable
	private IAdsWSConsumer				adsWsConsumer;

	private AgentWithServiceDto			superieurHierarchique;

	private List<AgentWithServiceDto>	equipeAgent;

	private FichePosteDto				ficheCourant;

	private boolean						estChef;

	@Wire
	private Listbox						resultGrid;

	private List<EaeFinalisationDto>	listeUrlEae;

	// pour l'arbre des services
	private EntiteDto					arbreService;
	private TreeModel<ServiceTreeNode>	arbre;

	private ProfilAgentDto				currentUser;

	@Init
	public void initEquipeAgent() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		AgentWithServiceDto result = sirhWsConsumer.getSuperieurHierarchique(currentUser.getAgent().getIdAgent());
		setSuperieurHierarchique(result);
		EstChefDto dto = sirhWsConsumer.isAgentChef(currentUser.getAgent().getIdAgent());
		setEstChef(dto.isEstResponsable());
		// si l'agent est chef
		if (isEstChef()) {
			EntiteDto tree = adsWsConsumer.getEntiteWithChildrenByIdEntite(currentUser.getIdServiceAds());
			setArbreService(tree);
			initModel();
			ouvreArbre();

		} else {
			// sinon
			List<AgentWithServiceDto> ag = sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(), null);
			setEquipeAgent(ag);
		}
	}

	@SuppressWarnings("unchecked")
	private void ouvreArbre() {
		// pour ouvrir l'arbre
		ServiceTreeNode node = getArbre().getChild(getArbre().getRoot(), 0);
		for (ServiceTreeNode enfant : node.getChildren()) {

			((Openable<ServiceTreeNode>) getArbre()).addOpenObject(enfant);
		}
		((Openable<ServiceTreeNode>) getArbre()).addOpenObject(node);
	}

	public String concatAgent(AgentWithServiceDto ag) {
		if (ag != null) {
			return ag.getCivilite() + " " + ag.getNom() + " " + transformPrenom(ag.getPrenom()) + " - " + ag.getPosition();
		} else {
			return "aucun";
		}
	}

	public String concatAgentSansCivilite(AgentWithServiceDto ag) {
		return ag.getNom() + " " + transformPrenom(ag.getPrenom()) + " - " + ag.getPosition();
	}

	public String transformPrenom(String prenom) {
		String premLettre = prenom.substring(0, 1).toUpperCase();
		String reste = prenom.substring(1, prenom.length()).toLowerCase();
		return premLettre + reste;
	}

	private void initModel() {
		setArbre(new ServiceTreeModel(getServiceTreeRoot()));
	}

	// create a FooNodes tree structure and return the root
	private ServiceTreeNode getServiceTreeRoot() {
		ServiceTreeNode root = new ServiceTreeNode(null, "", null);

		ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, getArbreService().getSigle(), getArbreService().getSigle());
		for (AgentWithServiceDto agent : sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(), getArbreService().getIdEntite())) {

			ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode, concatAgentSansCivilite(agent), agent.getIdAgent().toString());
			firstLevelNode.appendChild(agentLevelNode);
		}
		root.appendChild(firstLevelNode);

		addServiceTreeNodeFromThreeRecursive(root.getChildren().get(0), getArbreService());

		return root;
	}

	private void addServiceTreeNodeFromThreeRecursive(ServiceTreeNode root, EntiteDto entite) {

		for (EntiteDto entiteEnfant : entite.getEnfants()) {
			ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, entiteEnfant.getSigle(), entiteEnfant.getSigle());
			for (AgentWithServiceDto agent : sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(), entiteEnfant.getIdEntite())) {

				ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode, concatAgentSansCivilite(agent), agent.getIdAgent().toString());
				firstLevelNode.appendChild(agentLevelNode);
			}
			root.appendChild(firstLevelNode);
			addServiceTreeNodeFromThreeRecursive(firstLevelNode, entiteEnfant);
		}
	}

	@Listen("onSelect = #tree")
	@NotifyChange("ficheCourant")
	public void displayAgent(SelectEvent<Treeitem, String> event) throws NumberFormatException, Exception {
		Treeitem ref = event.getReference();
		ServiceTreeNode select = ref.getValue();
		ArrayList<FichePosteDto> t = new ArrayList<FichePosteDto>();
		if (!select.getChildren().isEmpty()) {
			t.add(null);
		} else {
			FichePosteDto result = sirhWsConsumer.getFichePoste(Integer.valueOf(select.getId()));
			setFicheCourant(result);
			t.add(getFicheCourant());
			// on charge les EAEs de l'agent concerné
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("idAgent", Integer.valueOf(select.getId()));
			BindUtils.postGlobalCommand(null, null, "chargeEAEAgent", args);
		}
		resultGrid.setModel(new ListModelList<FichePosteDto>(t));
	}

	@GlobalCommand
	@NotifyChange("listeUrlEae")
	public void chargeEAEAgent(@BindingParam("idAgent") Integer idAgent) throws Exception {
		List<EaeFinalisationDto> res = eaeWsConsumer.getEeaControle(idAgent);

		// on ne garde que les 3 dernieres EAEs
		if (res != null && res.size() > 3) {
			setListeUrlEae(res.subList(0, 3));
		} else {
			setListeUrlEae(res);
		}
	}

	@Command
	public void imprimeFDP(@BindingParam("ref") Listbox resultGrid) {
		FichePosteDto dto = (FichePosteDto) resultGrid.getModel().getElementAt(0);
		// on imprime la FDP de l'agent
		byte[] resp = sirhWsConsumer.imprimerFDP(dto.getIdFichePoste());
		Filedownload.save(resp, "application/pdf", "fichePoste_" + dto.getIdAgent());
	}

	public AgentWithServiceDto getSuperieurHierarchique() {
		return superieurHierarchique;
	}

	public void setSuperieurHierarchique(AgentWithServiceDto superieurHierarchique) {
		this.superieurHierarchique = superieurHierarchique;
	}

	public EntiteDto getArbreService() {
		return arbreService;
	}

	public void setArbreService(EntiteDto arbreService) {
		this.arbreService = arbreService;
	}

	public TreeModel<ServiceTreeNode> getArbre() {
		return arbre;
	}

	public void setArbre(TreeModel<ServiceTreeNode> _model) {
		this.arbre = _model;
	}

	public FichePosteDto getFicheCourant() {
		return ficheCourant;
	}

	public void setFicheCourant(FichePosteDto ficheCourant) {
		this.ficheCourant = ficheCourant;
	}

	public boolean isEstChef() {
		return estChef;
	}

	public void setEstChef(boolean estChef) {
		this.estChef = estChef;
	}

	public List<AgentWithServiceDto> getEquipeAgent() {
		return equipeAgent;
	}

	public void setEquipeAgent(List<AgentWithServiceDto> equipeAgent) {
		this.equipeAgent = equipeAgent;
	}

	public List<EaeFinalisationDto> getListeUrlEae() {
		return listeUrlEae;
	}

	public void setListeUrlEae(List<EaeFinalisationDto> listeUrlEae) {
		this.listeUrlEae = listeUrlEae;
	}

	public String getUrlFromAlfresco(EaeFinalisationDto dto) {
		if (dto == null || dto.getIdDocument() == null) {
			return "";
		}
		return AlfrescoCMISService.getUrlOfDocument(dto.getIdDocument());
	}

}
