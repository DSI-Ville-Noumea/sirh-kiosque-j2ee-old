package nc.noumea.mairie.kiosque.eae.viewModel;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TableauEaeViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private List<EaeListItemDto> tableauEae;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initTableauBord() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupère les info du tableau des EAEs
		List<EaeListItemDto> tableau = eaeWsConsumer.getTableauEae(currentUser.getAgent().getIdAgent());
		setTableauEae(tableau);
		// on initialise la taille du tableau
		setTailleListe("5");
	}

	@Command
	@NotifyChange({ "tableauEae" })
	public void doSearch() {
		List<EaeListItemDto> list = new ArrayList<EaeListItemDto>();
		if (getFilter() != null && !"".equals(getFilter())) {
			for (EaeListItemDto item : getTableauEae()) {
				// agent
				if (item.getAgentEvalue().getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgentEvalue().getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				// shd
				if (item.getAgentShd().getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgentShd().getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				// etat
				if (item.getEtat().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setTableauEae(list);
		} else {
			// on recupère les info du tableau des EAEs
			List<EaeListItemDto> tableau = eaeWsConsumer.getTableauEae(currentUser.getAgent().getIdAgent());
			setTableauEae(tableau);
		}
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public List<EaeListItemDto> getTableauEae() {
		return tableauEae;
	}

	public void setTableauEae(List<EaeListItemDto> tableauEae) {
		this.tableauEae = tableauEae;
	}

	public String getTailleListe() {
		return tailleListe;
	}

	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
