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
