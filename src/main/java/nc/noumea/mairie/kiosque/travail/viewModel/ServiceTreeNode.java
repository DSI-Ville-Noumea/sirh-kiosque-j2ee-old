package nc.noumea.mairie.kiosque.travail.viewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceTreeNode {
	private ServiceTreeNode _parent;
	private List<ServiceTreeNode> _children;
	private String _label = "";
	private String _id = null;

	public ServiceTreeNode(ServiceTreeNode parent, String label, String id) {
		_parent = parent;
		_label = label;
		_id = id;
	}

	public void setParent(ServiceTreeNode parent) {
		_parent = parent;
	}

	public ServiceTreeNode getParent() {
		return _parent;
	}

	public void appendChild(ServiceTreeNode child) {
		if (_children == null)
			_children = new ArrayList<ServiceTreeNode>();
		_children.add(child);
	}

	public List<ServiceTreeNode> getChildren() {
		if (_children == null)
			return Collections.emptyList();
		return _children;
	}

	public String getLabel() {
		return _label;
	}

	public String getId() {
		return _id;
	}

	public String toString() {
		return getLabel();
	}
}
