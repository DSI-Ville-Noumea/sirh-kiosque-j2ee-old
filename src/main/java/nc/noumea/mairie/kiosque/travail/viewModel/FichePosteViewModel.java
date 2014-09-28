package nc.noumea.mairie.kiosque.travail.viewModel;

import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FichePosteViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private FichePosteDto ficheCourant;

	@Init
	public void initFichePosteAgent() {
		FichePosteDto result = sirhWsConsumer.getFichePoste(9003041);
		setFicheCourant(result);
	}

	@Command
	public void imprimeFDP() {
		// on imprime la FDP de l'agent
		byte[] resp = sirhWsConsumer.imprimerFDP(getFicheCourant().getIdFichePoste());
		Filedownload.save(resp, "application/pdf", "fichePoste");
	}

	public FichePosteDto getFicheCourant() {
		return ficheCourant;
	}

	public void setFicheCourant(FichePosteDto ficheCourant) {
		this.ficheCourant = ficheCourant;
	}
}
