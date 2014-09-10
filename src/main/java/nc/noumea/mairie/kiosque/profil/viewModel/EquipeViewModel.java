package nc.noumea.mairie.kiosque.profil.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.profil.dto.ServiceTreeDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.TreeModel;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EquipeViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private AgentWithServiceDto superieurHierarchique;

	// pour l'arbre des services
	private List<ServiceTreeDto> arbreService;
	private TreeModel<ServiceTreeNode> model;

	@Init
	public void initEquipeAgent() {
		AgentWithServiceDto result = sirhWsConsumer.getSuperieurHierarchique(9005138);
		setSuperieurHierarchique(result);
		List<ServiceTreeDto> tree = sirhWsConsumer.getArbreServiceAgent(9005138);
		setArbreService(tree);
		initModel();
	}

	private void initModel() {
		setModel(new ServiceTreeModel(getServiceTreeRoot()));
	}

	// create a FooNodes tree structure and return the root
	private ServiceTreeNode getServiceTreeRoot() {
		ServiceTreeNode root = new ServiceTreeNode(null, "");
		for (ServiceTreeDto premierNiv : getArbreService()) {
			ServiceTreeNode firstLevelNode = new ServiceTreeNode(root, premierNiv.getSigle());
			for(AgentWithServiceDto ag : sirhWsConsumer.getAgentEquipe(9005138, premierNiv.getSigle())){
				ServiceTreeNode agentLevelNode = new ServiceTreeNode(firstLevelNode, ag.getNom()+" " +ag.getPrenom());
				firstLevelNode.appendChild(agentLevelNode);
			}
			for (ServiceTreeDto deuxNiv : premierNiv.getServicesEnfant()) {
				ServiceTreeNode secondLevelNode = new ServiceTreeNode(firstLevelNode, deuxNiv.getSigle());
				for(AgentWithServiceDto ag : sirhWsConsumer.getAgentEquipe(9005138, deuxNiv.getSigle())){
					ServiceTreeNode agentLevelNode = new ServiceTreeNode(secondLevelNode, ag.getNom()+" " +ag.getPrenom());
					secondLevelNode.appendChild(agentLevelNode);
				}
				firstLevelNode.appendChild(secondLevelNode);
			}
			root.appendChild(firstLevelNode);
		}
		return root;
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

	public TreeModel<ServiceTreeNode> getModel() {
		return model;
	}

	public void setModel(TreeModel<ServiceTreeNode> _model) {
		this.model = _model;
	}
}
