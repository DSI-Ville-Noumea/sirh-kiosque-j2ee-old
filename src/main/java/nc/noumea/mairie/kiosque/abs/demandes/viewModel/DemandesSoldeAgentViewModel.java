package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

import java.util.Date;

import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesSoldeAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private SoldeDto soldeCourant;

	private String title;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("agentCourant") AgentWithServiceDto agent) {
		setTitle("Solde de " + agent.getNom() + " " + agent.getPrenom());
		// on recupère le solde de l'agent
		FiltreSoldeDto filtreDto = new FiltreSoldeDto();
		filtreDto.setDateDebut(new Date());
		filtreDto.setDateFin(new Date());
		SoldeDto result = absWsConsumer.getAgentSolde(agent.getIdAgent(), filtreDto);
		setSoldeCourant(result);
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public SoldeDto getSoldeCourant() {
		return soldeCourant;
	}

	public void setSoldeCourant(SoldeDto soldeCourant) {
		this.soldeCourant = soldeCourant;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
