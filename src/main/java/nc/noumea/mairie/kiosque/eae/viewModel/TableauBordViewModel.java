package nc.noumea.mairie.kiosque.eae.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TableauBordViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private List<EaeDashboardItemDto> tableauBord;

	@Init
	public void initTableauBord() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupère les info du tableau de bord
		List<EaeDashboardItemDto> tableau = eaeWsConsumer.getTableauBord(currentUser.getAgent().getIdAgent());
		setTableauBord(tableau);
	}

	public List<EaeDashboardItemDto> getTableauBord() {
		return tableauBord;
	}

	public void setTableauBord(List<EaeDashboardItemDto> tableauBord) {
		this.tableauBord = tableauBord;
	}
}
