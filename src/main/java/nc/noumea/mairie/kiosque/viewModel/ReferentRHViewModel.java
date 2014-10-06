package nc.noumea.mairie.kiosque.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ReferentRHViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<ReferentRhDto> listeReferentRh;

	@Init
	public void initReferent() {
		List<ReferentRhDto> listRef = sirhWsConsumer.getListeReferentRH();
		setListeReferentRh(listRef);
	}

	public List<ReferentRhDto> getListeReferentRh() {
		return listeReferentRh;
	}

	public void setListeReferentRh(List<ReferentRhDto> listeReferentRh) {
		this.listeReferentRh = listeReferentRh;
	}
}
