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
