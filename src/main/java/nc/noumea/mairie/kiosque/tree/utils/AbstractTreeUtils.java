package nc.noumea.mairie.kiosque.tree.utils;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
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
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.travail.viewModel.ServiceTreeNode;

public abstract class AbstractTreeUtils {

	public static void updateServiceTreeNode(ServiceTreeNode node, ServiceTreeNode itemSelected, boolean isCheck, List<AgentDto> listeAgentsExistant) {

		if (null != node.getChildren() && !node.getChildren().isEmpty()) {

			for (ServiceTreeNode nodeEnfant : node.getChildren()) {

				if (null != itemSelected && nodeEnfant.getId().equals(itemSelected.getId())) {
					if (isCheck)
						setItemsChildSelected(nodeEnfant, listeAgentsExistant);
					else
						removeItemsChildSelected(nodeEnfant, listeAgentsExistant);
				}

				if (null == itemSelected && !isInteger(nodeEnfant.getId()) && !isAllNodeEnfantChecke(nodeEnfant)) {
					nodeEnfant.setSelectedDroitAbs(false);
				}

				if (null == itemSelected && !isInteger(nodeEnfant.getId()) && isAllNodeEnfantChecke(nodeEnfant)) {
					nodeEnfant.setSelectedDroitAbs(true);
				}

				updateServiceTreeNode(nodeEnfant, itemSelected, isCheck, listeAgentsExistant);
			}
		}
	}

	private static void setItemsChildSelected(ServiceTreeNode node, List<AgentDto> listeAgentsExistant) {
		for (ServiceTreeNode nodeEnfant : node.getChildren()) {
			if (isInteger(nodeEnfant.getId())) {
				nodeEnfant.setSelectedDroitAbs(true);

				AgentDto ag = new AgentDto();
				ag.setIdAgent(new Integer(nodeEnfant.getId()));
				if (!listeAgentsExistant.contains(ag)) {
					listeAgentsExistant.add(ag);
				}
			}
		}
	}

	private static void removeItemsChildSelected(ServiceTreeNode node, List<AgentDto> listeAgentsExistant) {
		for (ServiceTreeNode nodeEnfant : node.getChildren()) {
			if (isInteger(nodeEnfant.getId())) {
				nodeEnfant.setSelectedDroitAbs(false);

				AgentDto ag = new AgentDto();
				ag.setIdAgent(new Integer(nodeEnfant.getId()));
				if (listeAgentsExistant.contains(ag))
					listeAgentsExistant.remove(ag);
			}
		}
	}

	private static boolean isAllNodeEnfantChecke(ServiceTreeNode node) {
		// #20666 : si entité sans enfants alors on ne coche pas
		if (node.getChildren() == null || node.getChildren().isEmpty()) {
			return false;
		}

		boolean auMoins1Agent = false;
		for (ServiceTreeNode nodeEnfant : node.getChildren()) {
			if (isInteger(nodeEnfant.getId())) {
				auMoins1Agent = true;
				break;
			}
		}
		if (!auMoins1Agent)
			return false;

		for (ServiceTreeNode nodeEnfant : node.getChildren()) {
			if (isInteger(nodeEnfant.getId()) && !nodeEnfant.isSelectedDroitAbs()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public static List<ServiceTreeNode> getOpenObject(ServiceTreeNode nodeRoot) {

		List<ServiceTreeNode> result = new ArrayList<ServiceTreeNode>();
		result.add(nodeRoot);
		getListServiceTreeNodeFromTreeServiceTreeNode(result, nodeRoot);

		return result;
	}

	private static void getListServiceTreeNodeFromTreeServiceTreeNode(List<ServiceTreeNode> result, ServiceTreeNode node) {

		if (null != node && null != node.getChildren() && !node.getChildren().isEmpty()) {
			for (ServiceTreeNode nodeEnfant : node.getChildren()) {
				result.add(nodeEnfant);

				getListServiceTreeNodeFromTreeServiceTreeNode(result, nodeEnfant);
			}
		}
	}

	public static String concatAgentSansCivilite(AgentWithServiceDto ag) {
		return ag.getNom() + " " + transformPrenom(ag.getPrenom());
	}

	public static String transformPrenom(String prenom) {
		String premLettre = prenom.substring(0, 1).toUpperCase();
		String reste = prenom.substring(1, prenom.length()).toLowerCase();
		return premLettre + reste;
	}
}
