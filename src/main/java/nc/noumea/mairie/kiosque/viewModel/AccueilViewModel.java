package nc.noumea.mairie.kiosque.viewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AccueilViewModel {

	@Command
	public void referentRH() {
		
		HttpSession hSess = (HttpSession) ((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getSession();
		
		Session sess = Sessions.getCurrent();
		sess.setAttribute("currentUser", hSess.getAttribute("currentUser"));
		
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("referentRH.zul", null, null);
		win.doModal();
	}
}
