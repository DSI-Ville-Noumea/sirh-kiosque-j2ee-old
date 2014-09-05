package nc.noumea.mairie.kiosque.viewModel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AccueilViewModel {

	@Command
	public void referentRH() {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("referentRH.zul", null, null);
		win.doModal();
	}
}
