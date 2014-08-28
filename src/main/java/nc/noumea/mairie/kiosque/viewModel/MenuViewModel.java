package nc.noumea.mairie.kiosque.viewModel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel {

	@Wire
	Div contentDiv;

	@Command
	public void soldeAgent() {

		Executions.createComponents("/soldeAgent.zul", contentDiv, null);

	}
}
