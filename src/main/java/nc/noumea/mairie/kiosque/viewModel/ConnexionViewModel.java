package nc.noumea.mairie.kiosque.viewModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ConnexionViewModel {

	private Logger logger = LoggerFactory.getLogger(ConnexionViewModel.class);
	 
	private ProfilAgentDto currentUser;
	
	@Init
	public void initMenu() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
	}
	
	@Command
	public void disconnect() throws ServletException {
		// create a window programmatically and use it as a modal dialog.
		logger.debug("Disconnect User : " + currentUser.getAgent().getIdAgent());
		((HttpServletRequest)Executions.getCurrent().getNativeRequest()).logout();
		Executions.getCurrent().getSession().invalidate();
		Executions.sendRedirect("/deconnexion.zul");
	}

	public ProfilAgentDto getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(ProfilAgentDto currentUser) {
		this.currentUser = currentUser;
	}
	
}
