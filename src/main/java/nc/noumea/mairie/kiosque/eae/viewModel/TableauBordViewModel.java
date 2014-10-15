package nc.noumea.mairie.kiosque.eae.viewModel;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TableauBordViewModel {

	private ProfilAgentDto currentUser;

	@Init
	public void initTableauBord() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
	}
}
