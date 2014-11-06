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

import javax.servlet.ServletException;

import nc.noumea.mairie.kiosque.authentification.LDAP;
import nc.noumea.mairie.kiosque.dto.LightUserDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.viewModel.form.UserForm;
import nc.noumea.mairie.ws.IRadiWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ConnexionViewModel {

	private Logger logger = LoggerFactory.getLogger(ConnexionViewModel.class);

	private ProfilAgentDto currentUser;

	private UserForm userForm;

	@WireVariable
	private IRadiWSConsumer radiWSConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private String errorMessage;

	@Init
	public void initMenu() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		userForm = new UserForm();
	}

	@Command
	public void disconnect() throws ServletException {
		// create a window programmatically and use it as a modal dialog.
		logger.debug("Disconnect User : " + currentUser.getAgent().getIdAgent());
		Executions.getCurrent().getSession().setAttribute("logout", "logout");
		Executions.sendRedirect("/login.zul");
	}

	@Command
	@NotifyChange({ "userForm", "errorMessage" })
	public void connexion() {
		logger.debug("connexion User : " + userForm.getUser() + " password : " + userForm.getPassword());

		if (LDAP.controlerHabilitation(userForm.getUser(), userForm.getPassword())) {

			String remoteUser = userForm.getUser();

			LightUserDto userDto = radiWSConsumer.getAgentCompteADByLogin(remoteUser);
			if (null == userDto) {
				logger.debug("User not exist in Radi WS with RemoteUser : " + remoteUser);
				returnError();
			}

			if (0 == userDto.getEmployeeNumber()) {
				logger.debug("User not exist in Radi WS with RemoteUser : " + remoteUser);
				returnError();
			}

			ProfilAgentDto profilAgent = null;
			try {
				profilAgent = sirhWsConsumer.getEtatCivil(userDto.getEmployeeNumber());
			} catch (Exception e) {
				// le SIRH-WS ne semble pas repondre
				logger.debug("L'application SIRH-WS ne semble pas répondre.");
				returnError();
			}

			if (null == profilAgent) {
				logger.debug("ProfilAgent not exist in SIRH WS with EmployeeNumber : " + userDto.getEmployeeNumber());
				returnError();
			}

			Sessions.getCurrent().setAttribute("currentUser", profilAgent);
			logger.debug("Authentification du user ok : " + remoteUser);
			Executions.getCurrent().getSession().setAttribute("logout", null);
			Executions.sendRedirect("/index.zul");
		}

		returnError();
	}

	@Command
	@NotifyChange({ "userForm", "errorMessage" })
	public void reset() {
		userForm.reset();
		setErrorMessage(null);
	}

	private void returnError() {
		setErrorMessage("Le nom d'utilisateur ou le mot de passe est incorrect.");
		userForm.reset();
	}

	public String getPrenomAgent(String prenom) {
		String premierLettre = prenom.substring(0, 1).toUpperCase();
		String reste = prenom.substring(1, prenom.length()).toLowerCase();
		return premierLettre + reste;
	}

	public ProfilAgentDto getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(ProfilAgentDto currentUser) {
		this.currentUser = currentUser;
	}

	public UserForm getUserForm() {
		return userForm;
	}

	public void setUserForm(UserForm userForm) {
		this.userForm = userForm;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
