package nc.noumea.mairie.kiosque.abs.compteurs.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceEnum;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CompteursViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private String formulaireRecup;

	private String formulaireReposComp;

	/* Pour les filtres */

	private List<RefTypeAbsenceDto> listeTypeAbsenceFiltre;

	private RefTypeAbsenceDto typeAbsenceFiltre;

	private List<ServiceDto> listeServicesFiltre;

	private ServiceDto serviceFiltre;

	private List<AgentDto> listeAgentsFiltre;

	private AgentDto agentFiltre;

	@Init
	public void initCompteurs() {
		// on charge les types d'absences pour les filtres
		List<RefTypeAbsenceDto> filtreFamille = absWsConsumer.getRefGroupeAbsenceCompteur();
		setListeTypeAbsenceFiltre(filtreFamille);
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = absWsConsumer.getServicesCompteur(9003041);
		setListeServicesFiltre(filtreService);
		// pour les agents, on ne rempli pas la liste, elle le sera avec le
		// choix du service
		setListeAgentsFiltre(null);
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void chargeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = absWsConsumer.getAgentsCompteur(9003041, getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	@Command
	@NotifyChange({ "formulaireRecup","formulaireReposComp" })
	public void chargeFormulaire() {
		setFormulaireRecup(null);
		setFormulaireReposComp(null);
		if (getTypeAbsenceFiltre().getIdRefTypeAbsence() == RefTypeAbsenceEnum.RECUP.getValue()) {
			setFormulaireRecup("Récupérations");
		} else if (getTypeAbsenceFiltre().getIdRefTypeAbsence() == RefTypeAbsenceEnum.REPOS_COMP.getValue()) {
			setFormulaireReposComp("Repos compensateur");
		}
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public List<RefTypeAbsenceDto> getListeTypeAbsenceFiltre() {
		return listeTypeAbsenceFiltre;
	}

	public void setListeTypeAbsenceFiltre(List<RefTypeAbsenceDto> listeTypeAbsenceFiltre) {
		this.listeTypeAbsenceFiltre = listeTypeAbsenceFiltre;
	}

	public RefTypeAbsenceDto getTypeAbsenceFiltre() {
		return typeAbsenceFiltre;
	}

	public void setTypeAbsenceFiltre(RefTypeAbsenceDto typeAbsenceFiltre) {
		this.typeAbsenceFiltre = typeAbsenceFiltre;
	}

	public List<ServiceDto> getListeServicesFiltre() {
		return listeServicesFiltre;
	}

	public void setListeServicesFiltre(List<ServiceDto> listeServicesFiltre) {
		this.listeServicesFiltre = listeServicesFiltre;
	}

	public ServiceDto getServiceFiltre() {
		return serviceFiltre;
	}

	public void setServiceFiltre(ServiceDto serviceFiltre) {
		this.serviceFiltre = serviceFiltre;
	}

	public List<AgentDto> getListeAgentsFiltre() {
		return listeAgentsFiltre;
	}

	public void setListeAgentsFiltre(List<AgentDto> listeAgentsFiltre) {
		this.listeAgentsFiltre = listeAgentsFiltre;
	}

	public AgentDto getAgentFiltre() {
		return agentFiltre;
	}

	public void setAgentFiltre(AgentDto agentFiltre) {
		this.agentFiltre = agentFiltre;
	}

	public String getFormulaireRecup() {
		return formulaireRecup;
	}

	public void setFormulaireRecup(String formulaireRecup) {
		this.formulaireRecup = formulaireRecup;
	}

	public String getFormulaireReposComp() {
		return formulaireReposComp;
	}

	public void setFormulaireReposComp(String formulaireReposComp) {
		this.formulaireReposComp = formulaireReposComp;
	}

}
