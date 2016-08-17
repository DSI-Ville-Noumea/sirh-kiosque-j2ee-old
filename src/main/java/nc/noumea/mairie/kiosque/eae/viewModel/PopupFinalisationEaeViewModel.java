package nc.noumea.mairie.kiosque.eae.viewModel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PopupFinalisationEaeViewModel {

	private EaeListItemDto eaeCourant;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("eaeCourant") EaeListItemDto eae) {
		// on recupere l'EAE selectionné
		setEaeCourant(eae);
	}

	public EaeListItemDto getEaeCourant() {
		return eaeCourant;
	}

	public void setEaeCourant(EaeListItemDto eaeCourant) {
		this.eaeCourant = eaeCourant;
	}
}
