package nc.noumea.mairie.kiosque.profil.viewModel;

import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

public class ServiceTreeModel extends AbstractTreeModel<ServiceTreeNode> {
	private static final long serialVersionUID = 1L;

	public ServiceTreeModel(ServiceTreeNode root) {
		super(root);
	}

	public boolean isLeaf(ServiceTreeNode node) {
		return ((ServiceTreeNode) node).getChildren().size() == 0;
	}

	public ServiceTreeNode getChild(ServiceTreeNode parent, int index) {
		return ((ServiceTreeNode) parent).getChildren().get(index);
	}

	public int getChildCount(ServiceTreeNode parent) {
		return ((ServiceTreeNode) parent).getChildren().size();
	}

	public int getIndexOfChild(ServiceTreeNode parent, ServiceTreeNode child) {
		List<ServiceTreeNode> children = ((ServiceTreeNode) parent).getChildren();
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).equals(children))
				return i;
		}
		return -1;
	}
};
