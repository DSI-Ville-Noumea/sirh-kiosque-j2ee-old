package nc.noumea.mairie.kiosque.eae.dto;

public class EaeAppreciationDto {

	private boolean estEncadrant;
	private int idEae;
	private String[] managerialEvaluateur;
	private String[] managerialEvalue;
	private String[] resultatsEvaluateur;
	private String[] resultatsEvalue;
	private String[] savoirEtreEvaluateur;
	private String[] savoirEtreEvalue;
	private String[] techniqueEvaluateur;
	private String[] techniqueEvalue;

	public boolean isEstEncadrant() {
		return estEncadrant;
	}

	public void setEstEncadrant(boolean estEncadrant) {
		this.estEncadrant = estEncadrant;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String[] getManagerialEvaluateur() {
		return managerialEvaluateur;
	}

	public void setManagerialEvaluateur(String[] managerialEvaluateur) {
		this.managerialEvaluateur = managerialEvaluateur;
	}

	public String[] getManagerialEvalue() {
		return managerialEvalue;
	}

	public void setManagerialEvalue(String[] managerialEvalue) {
		this.managerialEvalue = managerialEvalue;
	}

	public String[] getResultatsEvaluateur() {
		return resultatsEvaluateur;
	}

	public void setResultatsEvaluateur(String[] resultatsEvaluateur) {
		this.resultatsEvaluateur = resultatsEvaluateur;
	}

	public String[] getResultatsEvalue() {
		return resultatsEvalue;
	}

	public void setResultatsEvalue(String[] resultatsEvalue) {
		this.resultatsEvalue = resultatsEvalue;
	}

	public String[] getSavoirEtreEvaluateur() {
		return savoirEtreEvaluateur;
	}

	public void setSavoirEtreEvaluateur(String[] savoirEtreEvaluateur) {
		this.savoirEtreEvaluateur = savoirEtreEvaluateur;
	}

	public String[] getSavoirEtreEvalue() {
		return savoirEtreEvalue;
	}

	public void setSavoirEtreEvalue(String[] savoirEtreEvalue) {
		this.savoirEtreEvalue = savoirEtreEvalue;
	}

	public String[] getTechniqueEvaluateur() {
		return techniqueEvaluateur;
	}

	public void setTechniqueEvaluateur(String[] techniqueEvaluateur) {
		this.techniqueEvaluateur = techniqueEvaluateur;
	}

	public String[] getTechniqueEvalue() {
		return techniqueEvalue;
	}

	public void setTechniqueEvalue(String[] techniqueEvalue) {
		this.techniqueEvalue = techniqueEvalue;
	}
}
