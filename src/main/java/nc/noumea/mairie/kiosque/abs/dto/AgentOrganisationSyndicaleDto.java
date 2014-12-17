package nc.noumea.mairie.kiosque.abs.dto;

public class AgentOrganisationSyndicaleDto {

	private Integer idAgent;
	private boolean actif;

	public AgentOrganisationSyndicaleDto() {

	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}
}
