package nc.noumea.mairie.kiosque.eae.dto;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;

public class EaeListItemDto {

	private Integer idEae;
	private AgentDto agentEvalue;
	private List<AgentDto> eaeEvaluateurs;
	private AgentDto agentDelegataire;
	private EaeEtatEnum etat;
	private boolean cap;
	private String avisShd;
	private boolean docAttache;
	private Date dateCreation;
	private Date dateFinalisation;
	private Date dateControle;
	private boolean droitInitialiser;
	private boolean droitAcceder;
	private boolean droitDemarrer;
	private boolean droitAffecterDelegataire;
	private boolean droitImprimerBirt;
	private boolean droitImprimerGed;
	private String idDocumentGed;
	private boolean estDetache;

	private String directionService;
	private String sectionService;
	private String service;
	private AgentDto agentShd;

	public Integer getIdEae() {
		return idEae;
	}

	public void setIdEae(Integer idEae) {
		this.idEae = idEae;
	}

	public AgentDto getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(AgentDto agentEvalue) {
		this.agentEvalue = agentEvalue;
	}

	public List<AgentDto> getEaeEvaluateurs() {
		return eaeEvaluateurs;
	}

	public void setEaeEvaluateurs(List<AgentDto> eaeEvaluateurs) {
		this.eaeEvaluateurs = eaeEvaluateurs;
	}

	public AgentDto getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(AgentDto agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}

	public EaeEtatEnum getEtat() {
		return etat;
	}

	public void setEtat(EaeEtatEnum etat) {
		this.etat = etat;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public String getAvisShd() {
		return avisShd;
	}

	public void setAvisShd(String avisShd) {
		this.avisShd = avisShd;
	}

	public boolean isDocAttache() {
		return docAttache;
	}

	public void setDocAttache(boolean docAttache) {
		this.docAttache = docAttache;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateFinalisation() {
		return dateFinalisation;
	}

	public void setDateFinalisation(Date dateFinalisation) {
		this.dateFinalisation = dateFinalisation;
	}

	public Date getDateControle() {
		return dateControle;
	}

	public void setDateControle(Date dateControle) {
		this.dateControle = dateControle;
	}

	public boolean isDroitInitialiser() {
		return droitInitialiser;
	}

	public void setDroitInitialiser(boolean droitInitialiser) {
		this.droitInitialiser = droitInitialiser;
	}

	public boolean isDroitAcceder() {
		return droitAcceder;
	}

	public void setDroitAcceder(boolean droitAcceder) {
		this.droitAcceder = droitAcceder;
	}

	public boolean isDroitDemarrer() {
		return droitDemarrer;
	}

	public void setDroitDemarrer(boolean droitDemarrer) {
		this.droitDemarrer = droitDemarrer;
	}

	public boolean isDroitAffecterDelegataire() {
		return droitAffecterDelegataire;
	}

	public void setDroitAffecterDelegataire(boolean droitAffecterDelegataire) {
		this.droitAffecterDelegataire = droitAffecterDelegataire;
	}

	public boolean isDroitImprimerBirt() {
		return droitImprimerBirt;
	}

	public void setDroitImprimerBirt(boolean droitImprimerBirt) {
		this.droitImprimerBirt = droitImprimerBirt;
	}

	public boolean isDroitImprimerGed() {
		return droitImprimerGed;
	}

	public void setDroitImprimerGed(boolean droitImprimerGed) {
		this.droitImprimerGed = droitImprimerGed;
	}

	public String getIdDocumentGed() {
		return idDocumentGed;
	}

	public void setIdDocumentGed(String idDocumentGed) {
		this.idDocumentGed = idDocumentGed;
	}

	public boolean isEstDetache() {
		return estDetache;
	}

	public void setEstDetache(boolean estDetache) {
		this.estDetache = estDetache;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getSectionService() {
		return sectionService;
	}

	public void setSectionService(String sectionService) {
		this.sectionService = sectionService;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public AgentDto getAgentShd() {
		return agentShd;
	}

	public void setAgentShd(AgentDto agentShd) {
		this.agentShd = agentShd;
	}
}
