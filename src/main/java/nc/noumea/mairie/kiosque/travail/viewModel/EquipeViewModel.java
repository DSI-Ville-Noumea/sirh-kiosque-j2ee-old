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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.cmis.ISharepointService;
import nc.noumea.mairie.kiosque.cmis.SharepointDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.EstChefDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.travail.dto.ServiceTreeDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EquipeViewModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@WireVariable
	private ISharepointService sharepointConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private AgentWithServiceDto superieurHierarchique;

	private List<AgentWithServiceDto> equipeAgent;

	private FichePosteDto ficheCourant;

	private boolean estChef;

	@Wire
	private Listbox resultGrid;

	private List<SharepointDto> listeUrlEae;

	// pour l'arbre des services
	private List<ServiceTreeDto> arbreService;
	private TreeModel<ServiceTreeNode> arbre;

	private ProfilAgentDto currentUser;

	@Init
	public void initEquipeAgent() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		AgentWithServiceDto result = sirhWsConsumer.getSuperieurHierarchique(currentUser.getAgent().getIdAgent());
		setSuperieurHierarchique(result);
		EstChefDto dto = sirhWsConsumer.isAgentChef(currentUser.getAgent().getIdAgent());
		setEstChef(dto.isEstResponsable());
		// si l'agent est chef
		if (isEstChef()) {
			List<ServiceTreeDto> tree = sirhWsConsumer.getArbreServiceAgent(currentUser.getAgent().getIdAgent());
			setArbreService(tree);
			initModel();
		} else {
			// sinon
			List<AgentWithServiceDto> ag = sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(), null);
			setEquipeAgent(ag);
		}
	}

	public String concatAgent(AgentWithServiceDto ag) {
		return ag.getCivilite() + " " + ag.getNom() + " " + transformPrenom(ag.getPrenom()) + " - " + ag.getPosition();
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
		for (ServiceTreeDto premierNiv : getArbreService()) {
			ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, premierNiv.getSigle(), premierNiv.getSigle());
			for (AgentWithServiceDto ag : sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(),
					premierNiv.getService())) {
				ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode, concatAgentSansCivilite(ag), ag
						.getIdAgent().toString());
				firstLevelNode.appendChild(agentLevelNode);
			}
			for (ServiceTreeDto deuxNiv : premierNiv.getServicesEnfant()) {
				ServiceTreeNode secondLevelNode = new ServiceTreeNode(firstLevelNode, deuxNiv.getSigle(),
						deuxNiv.getSigle());
				for (AgentWithServiceDto ag : sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(),
						deuxNiv.getService())) {
					ServiceTreeNode agentLevelNode = new ServiceTreeNode(secondLevelNode, concatAgentSansCivilite(ag),
							ag.getIdAgent().toString());
					secondLevelNode.appendChild(agentLevelNode);
				}
				firstLevelNode.appendChild(secondLevelNode);
			}
			root.appendChild(firstLevelNode);
		}
		return root;
	}

	@Command
	public void visuEAE(@BindingParam("ref") String url) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, String> args = new HashMap<String, String>();
		args.put("url", url);
		Window win = (Window) Executions.createComponents("/travail/visuEae.zul", null, args);
		win.doModal();
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
		List<SharepointDto> res = sharepointConsumer.getAllEae(idAgent);

		// on tri la liste par année
		Collections.sort(res, new Comparator<SharepointDto>() {
			@Override
			public int compare(SharepointDto o1, SharepointDto o2) {
				return o2.getAnnee().compareTo(o1.getAnnee());
			}

		});
		setListeUrlEae(res);
	}

	@Command
	public void imprimeFDP(@BindingParam("ref") Grid resultGrid) {
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

	public List<ServiceTreeDto> getArbreService() {
		return arbreService;
	}

	public void setArbreService(List<ServiceTreeDto> arbreService) {
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

	public List<SharepointDto> getListeUrlEae() {
		return listeUrlEae;
	}

	public void setListeUrlEae(List<SharepointDto> listeUrlEae) {
		this.listeUrlEae = listeUrlEae;
	}

}
