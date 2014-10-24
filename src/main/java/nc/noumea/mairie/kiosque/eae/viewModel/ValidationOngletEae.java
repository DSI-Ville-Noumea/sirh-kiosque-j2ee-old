package nc.noumea.mairie.kiosque.eae.viewModel;

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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.validation.ValidationMessage;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

public class ValidationOngletEae {
	private List<ValidationMessage> errorsMessages = null;
	private List<ValidationMessage> infosMessages = null;
	private Tab tabChoisi;

	@Init
	public void init(@ExecutionArgParam("errors") List<ValidationMessage> errors,
			@ExecutionArgParam("infos") List<ValidationMessage> infos, @ExecutionArgParam("tab") Tab tab) {
		setErrorsMessages(errors);
		setInfosMessages(infos);
		setTabChoisi(tab);
	}

	@Command
	public void engistreOnglet(@BindingParam("win") Window window) {
		BindUtils.postGlobalCommand(null, null, "engistreOnglet", null);
		window.detach();
	}

	@Command
	public void annulerEngistreOnglet(@BindingParam("win") Window window) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("tab", getTabChoisi());
		BindUtils.postGlobalCommand(null, null, "annulerEngistreOnglet", args);
		window.detach();
	}

	public List<ValidationMessage> getErrorsMessages() {
		return errorsMessages;
	}

	public void setErrorsMessages(List<ValidationMessage> errorsMessages) {
		this.errorsMessages = errorsMessages;
	}

	public List<ValidationMessage> getInfosMessages() {
		return infosMessages;
	}

	public void setInfosMessages(List<ValidationMessage> infosMessages) {
		this.infosMessages = infosMessages;
	}

	public Tab getTabChoisi() {
		return tabChoisi;
	}

	public void setTabChoisi(Tab tabChoisi) {
		this.tabChoisi = tabChoisi;
	}
}
