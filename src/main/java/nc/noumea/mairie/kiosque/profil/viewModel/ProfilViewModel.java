package nc.noumea.mairie.kiosque.profil.viewModel;

import nc.noumea.mairie.kiosque.profil.dto.EtatCivilDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProfilViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;
	
	private EtatCivilDto agentCourant;

	@Init
	public void initProfilAgent() {
		EtatCivilDto result = sirhWsConsumer.getEtatCivil(9005138);
		setAgentCourant(result);
	}

	public EtatCivilDto getAgentCourant() {
		return agentCourant;
	}

	public void setAgentCourant(EtatCivilDto agentCourant) {
		this.agentCourant = agentCourant;
	}
}
