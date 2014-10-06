package nc.noumea.mairie.kiosque.dto;

public class ReferentRhDto {

	private String servi;
	private String sigleService;
	private Integer idAgentReferent;
	private String prenomAgentReferent;
	private Integer numeroTelephone;

	public String getServi() {
		return servi;
	}

	public void setServi(String servi) {
		this.servi = servi;
	}

	public Integer getIdAgentReferent() {
		return idAgentReferent;
	}

	public void setIdAgentReferent(Integer idAgentReferent) {
		this.idAgentReferent = idAgentReferent;
	}

	public Integer getNumeroTelephone() {
		return numeroTelephone;
	}

	public void setNumeroTelephone(Integer numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}

	public String getSigleService() {
		return sigleService;
	}

	public void setSigleService(String sigleService) {
		this.sigleService = sigleService;
	}

	public String getPrenomAgentReferent() {
		return prenomAgentReferent;
	}

	public void setPrenomAgentReferent(String prenomAgentReferent) {
		this.prenomAgentReferent = prenomAgentReferent;
	}


}
