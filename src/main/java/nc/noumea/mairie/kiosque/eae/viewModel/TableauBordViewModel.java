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
		List<EaeDashboardItemDto> tableau = eaeWsConsumer.getTableauBord(9002990);
		setTableauBord(tableau);
	}

	public String totalNonAffecte() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonAffecte();
		}
		return String.valueOf(total);
	}

	public String totalNonDebute() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonDebute();
		}
		return String.valueOf(total);
	}

	public String totalCree() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getCree();
		}
		return String.valueOf(total);
	}

	public String totalEnCours() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getEnCours();
		}
		return String.valueOf(total);
	}

	public String totalFinalise() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getFinalise();
		}
		return String.valueOf(total);
	}

	public String totalFige() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getFige();
		}
		return String.valueOf(total);
	}

	public String totalEae() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonAffecte() + dto.getNonDebute() + dto.getCree() + dto.getEnCours() + dto.getFinalise()
					+ dto.getFige();
		}
		return String.valueOf(total);
	}

	public String totalNonDefini() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonDefini();
		}
		return String.valueOf(total);
	}

	public String totalMini() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getMini();
		}
		return String.valueOf(total);
	}

	public String totalMoy() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getMoy();
		}
		return String.valueOf(total);
	}

	public String totalMaxi() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getMaxi();
		}
		return String.valueOf(total);
	}

	public String totalChgtClasse() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getChangClasse();
		}
		return String.valueOf(total);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String totalEae(EaeDashboardItemDto dto) {
		int total = dto.getNonAffecte() + dto.getNonDebute() + dto.getCree() + dto.getEnCours() + dto.getFinalise()
				+ dto.getFige();
		return String.valueOf(total);
	}

	public List<EaeDashboardItemDto> getTableauBord() {
		return tableauBord;
	}

	public void setTableauBord(List<EaeDashboardItemDto> tableauBord) {
		this.tableauBord = tableauBord;
	}
}
