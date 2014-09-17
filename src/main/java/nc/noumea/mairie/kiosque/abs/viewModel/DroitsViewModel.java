package nc.noumea.mairie.kiosque.abs.viewModel;

import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DroitsViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private Tab tabCourant;

	@Init
	public void initDroits() {
	}

	@Command
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
	}

	@Command
	public void changeVue(@BindingParam("tab") Tab tab) {
		// on sauvegarde l'onglet
		setTabCourant(tab);
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
	}
}
