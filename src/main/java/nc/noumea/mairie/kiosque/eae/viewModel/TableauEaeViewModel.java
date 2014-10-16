package nc.noumea.mairie.kiosque.eae.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TableauEaeViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private List<EaeListItemDto> tableauEae;

	@Init
	public void initTableauBord() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupère les info du tableau de bord
		List<EaeListItemDto> tableau = eaeWsConsumer.getTableauEae(currentUser.getAgent().getIdAgent());
		setTableauEae(tableau);
	}

	public List<EaeListItemDto> getTableauEae() {
		return tableauEae;
	}

	public void setTableauEae(List<EaeListItemDto> tableauEae) {
		this.tableauEae = tableauEae;
	}
}
