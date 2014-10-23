package nc.noumea.mairie.kiosque.viewModel;

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
	@NotifyChange({ "userForm" , "errorMessage" })
	public void connexion() {
		logger.debug("connexion User : " + userForm.getUser() + " password : "
				+ userForm.getPassword());

		if (LDAP.controlerHabilitation(userForm.getUser(),
				userForm.getPassword())) {

			String remoteUser = userForm.getUser();
			
			LightUserDto userDto = radiWSConsumer
					.getAgentCompteADByLogin(remoteUser);
			if (null == userDto) {
				logger.debug("User not exist in Radi WS with RemoteUser : "
						+ remoteUser);
				returnError();
			}

			if (0 == userDto.getEmployeeNumber()) {
				logger.debug("User not exist in Radi WS with RemoteUser : "
						+ remoteUser);
				returnError();
			}

			ProfilAgentDto profilAgent = null;
			try {
				profilAgent = sirhWsConsumer.getEtatCivil(userDto
						.getEmployeeNumber());
			} catch (Exception e) {
				// le SIRH-WS ne semble pas repondre
				logger.debug("L'application SIRH-WS ne semble pas répondre.");
				returnError();
			}

			if (null == profilAgent) {
				logger.debug("ProfilAgent not exist in SIRH WS with EmployeeNumber : "
						+ userDto.getEmployeeNumber());
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
	@NotifyChange({ "userForm" , "errorMessage" })
	public void reset() {
		userForm.reset();
		setErrorMessage(null);
	}
	
	private void returnError() {
		setErrorMessage("Le nom d'utilisateur ou le mot de passe est incorrect.");
		userForm.reset();
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
