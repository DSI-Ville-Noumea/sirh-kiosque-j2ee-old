package nc.noumea.mairie.kiosque.viewModel;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ConnexionViewModel {

	private ProfilAgentDto currentUser;
	
	@Init
	public void initMenu() {
		
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
	}
	
	@Command
	public void disconnect() {
		// create a window programmatically and use it as a modal dialog.
		Session sess = Sessions.getCurrent();
		sess.invalidate();
	}
	
	@Command
	public void reconnectWithOtherUser() {
		// create a window programmatically and use it as a modal dialog.
		Executions.getCurrent().getUserPrincipal();
		Session sess = Sessions.getCurrent();
		sess.invalidate();
	}

	public ProfilAgentDto getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(ProfilAgentDto currentUser) {
		this.currentUser = currentUser;
	}
	
	
}
