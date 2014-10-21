package nc.noumea.mairie.kiosque.eae.dto;

public class EaeAutoEvaluationDto {

	private int idEae;
	private String acquis;
	private String particularites;
	private String succesDifficultes;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String getAcquis() {
		return acquis;
	}

	public void setAcquis(String acquis) {
		this.acquis = acquis;
	}

	public String getParticularites() {
		return particularites;
	}

	public void setParticularites(String particularites) {
		this.particularites = particularites;
	}

	public String getSuccesDifficultes() {
		return succesDifficultes;
	}

	public void setSuccesDifficultes(String succesDifficultes) {
		this.succesDifficultes = succesDifficultes;
	}
}
