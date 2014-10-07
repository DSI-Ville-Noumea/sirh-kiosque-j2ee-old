package nc.noumea.mairie.kiosque.ptg.viewModel;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImpressionFichesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	@Init
	public void initImpressionFiches() {

		ProfilAgentDto currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
	}
}
