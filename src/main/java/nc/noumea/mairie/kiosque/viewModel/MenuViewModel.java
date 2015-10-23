package nc.noumea.mairie.kiosque.viewModel;

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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.noumea.mairie.kiosque.cmis.ISharepointService;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel extends AbstractViewModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7929474744616954278L;

	@WireVariable
	private ISharepointService sharepointConsumer;

	private boolean droitsModulePointage;

	@WireVariable
	private EnvironnementService environnementService;

	private boolean afficheRecette;

	@Init
	public void initMenu() {

		if (environnementService.isRecette()) {
			setAfficheRecette(true);
		} else {
			setAfficheRecette(false);
		}

		/* Pour les pointages */
		setDroitsModulePointage(getDroitsPointage() == null ? false : getDroitsPointage().isApprobation() || getDroitsPointage().isFiches() || getDroitsPointage().isGestionDroitsAcces()
				|| getDroitsPointage().isSaisie() || getDroitsPointage().isVisualisation());
	}

	@Command
	public void changeEcran(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Map<String, Div> args = new HashMap<String, Div>();
		args.put("div", div);
		Executions.createComponents(page + ".zul", div, args);
	}

	@Command
	public void eaeSharepoint(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		// if (currentUser.getAgent().getIdAgent() == 9005138 ||
		// currentUser.getAgent().getIdAgent() == 9005131) {
		// Map<String, Div> args = new HashMap<String, Div>();
		// args.put("div", div);
		//
		// div.getChildren().clear();
		// Executions.createComponents(page + ".zul", div, args);
		// } else {
		div.getChildren().clear();
		// redmine #14077 : on redirige vers la page d'accueil
		Map<String, Div> args = new HashMap<String, Div>();
		args.put("div", div);
		Executions.createComponents("accueil.zul", div, args);
		Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlEaeApprobateur(), "_blank");
		// }
	}

	@Command
	public void tableauBordSharepoint(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		// if (currentUser.getAgent().getIdAgent() == 9005138 ||
		// currentUser.getAgent().getIdAgent() == 9005131) {
		// div.getChildren().clear();
		// Executions.createComponents(page + ".zul", div, null);
		// } else {
		div.getChildren().clear();
		// redmine #14077 : on redirige vers la page d'accueil
		Map<String, Div> args = new HashMap<String, Div>();
		args.put("div", div);
		Executions.createComponents("accueil.zul", div, args);
		Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlTableauBordApprobateur(), "_blank");
		// }
	}

	public boolean isDroitsModulePointage() {
		return droitsModulePointage;
	}

	public void setDroitsModulePointage(boolean droitsModulePointage) {
		this.droitsModulePointage = droitsModulePointage;
	}

	public boolean isAfficheRecette() {
		return afficheRecette;
	}

	public void setAfficheRecette(boolean afficheRecette) {
		this.afficheRecette = afficheRecette;
	}
}
