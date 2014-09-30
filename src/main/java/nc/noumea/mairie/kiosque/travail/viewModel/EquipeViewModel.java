package nc.noumea.mairie.kiosque.travail.viewModel;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.EstChefDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.travail.dto.ServiceTreeDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EquipeViewModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private AgentWithServiceDto superieurHierarchique;

	private List<AgentWithServiceDto> equipeAgent;

	private FichePosteDto ficheCourant;

	private boolean estChef;

	@Wire
	private Grid resultGrid;

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

	private void initModel() {
		setArbre(new ServiceTreeModel(getServiceTreeRoot()));
	}

	// create a FooNodes tree structure and return the root
	private ServiceTreeNode getServiceTreeRoot() {
		ServiceTreeNode root = new ServiceTreeNode(null, "", null);
		for (ServiceTreeDto premierNiv : getArbreService()) {
			ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, premierNiv.getSigle(), premierNiv.getService());
			for (AgentWithServiceDto ag : sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(), premierNiv.getSigle())) {
				ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode,
						ag.getNom() + " " + ag.getPrenom(), ag.getIdAgent().toString());
				firstLevelNode.appendChild(agentLevelNode);
			}
			for (ServiceTreeDto deuxNiv : premierNiv.getServicesEnfant()) {
				ServiceTreeNode secondLevelNode = new ServiceTreeNode(firstLevelNode, deuxNiv.getSigle(),
						deuxNiv.getService());
				for (AgentWithServiceDto ag : sirhWsConsumer.getAgentEquipe(currentUser.getAgent().getIdAgent(), deuxNiv.getSigle())) {
					ServiceTreeNode agentLevelNode = new ServiceTreeNode(secondLevelNode, ag.getNom() + " "
							+ ag.getPrenom(), ag.getIdAgent().toString());
					secondLevelNode.appendChild(agentLevelNode);
				}
				firstLevelNode.appendChild(secondLevelNode);
			}
			root.appendChild(firstLevelNode);
		}
		return root;
	}

	@Listen("onSelect = #tree")
	public void displayAgent(SelectEvent<Treeitem, String> event) {
		Treeitem ref = event.getReference();
		ServiceTreeNode select = ref.getValue();
		ArrayList<FichePosteDto> t = new ArrayList<FichePosteDto>();
		if (!select.getChildren().isEmpty()) {
			t.add(null);
		} else {
			FichePosteDto result = sirhWsConsumer.getFichePoste(Integer.valueOf(select.getId()));
			setFicheCourant(result);

			t.add(getFicheCourant());
		}
		resultGrid.setModel(new ListModelList<FichePosteDto>(t));
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
}
