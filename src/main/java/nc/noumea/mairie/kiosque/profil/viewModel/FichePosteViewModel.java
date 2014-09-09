package nc.noumea.mairie.kiosque.profil.viewModel;

import nc.noumea.mairie.kiosque.profil.dto.FichePosteDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FichePosteViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private FichePosteDto ficheCourant;

	@Init
	public void initFichePosteAgent() {
		FichePosteDto result = sirhWsConsumer.getFichePoste(9005138);
		setFicheCourant(result);
	}

	public FichePosteDto getFicheCourant() {
		return ficheCourant;
	}

	public void setFicheCourant(FichePosteDto ficheCourant) {
		this.ficheCourant = ficheCourant;
	}
}
