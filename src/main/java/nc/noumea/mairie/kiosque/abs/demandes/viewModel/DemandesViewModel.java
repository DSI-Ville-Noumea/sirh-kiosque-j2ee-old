package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@Init
	public void initDemandes() {
	}

}
