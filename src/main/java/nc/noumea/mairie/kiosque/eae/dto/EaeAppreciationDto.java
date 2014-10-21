package nc.noumea.mairie.kiosque.eae.dto;

public class EaeAppreciationDto {

	private boolean estEncadrant;
	private int idEae;
	private ItemAppreciationDto managerialEvaluateur;
	private ItemAppreciationDto managerialEvalue;
	private ItemAppreciationDto resultatsEvaluateur;
	private ItemAppreciationDto resultatsEvalue;
	private ItemAppreciationDto savoirEtreEvaluateur;
	private ItemAppreciationDto savoirEtreEvalue;
	private ItemAppreciationDto techniqueEvaluateur;
	private ItemAppreciationDto techniqueEvalue;

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

	public ItemAppreciationDto getManagerialEvaluateur() {
		return managerialEvaluateur;
	}

	public void setManagerialEvaluateur(ItemAppreciationDto managerialEvaluateur) {
		this.managerialEvaluateur = managerialEvaluateur;
	}

	public ItemAppreciationDto getManagerialEvalue() {
		return managerialEvalue;
	}

	public void setManagerialEvalue(ItemAppreciationDto managerialEvalue) {
		this.managerialEvalue = managerialEvalue;
	}

	public ItemAppreciationDto getResultatsEvaluateur() {
		return resultatsEvaluateur;
	}

	public void setResultatsEvaluateur(ItemAppreciationDto resultatsEvaluateur) {
		this.resultatsEvaluateur = resultatsEvaluateur;
	}

	public ItemAppreciationDto getResultatsEvalue() {
		return resultatsEvalue;
	}

	public void setResultatsEvalue(ItemAppreciationDto resultatsEvalue) {
		this.resultatsEvalue = resultatsEvalue;
	}

	public ItemAppreciationDto getSavoirEtreEvaluateur() {
		return savoirEtreEvaluateur;
	}

	public void setSavoirEtreEvaluateur(ItemAppreciationDto savoirEtreEvaluateur) {
		this.savoirEtreEvaluateur = savoirEtreEvaluateur;
	}

	public ItemAppreciationDto getSavoirEtreEvalue() {
		return savoirEtreEvalue;
	}

	public void setSavoirEtreEvalue(ItemAppreciationDto savoirEtreEvalue) {
		this.savoirEtreEvalue = savoirEtreEvalue;
	}

	public ItemAppreciationDto getTechniqueEvaluateur() {
		return techniqueEvaluateur;
	}

	public void setTechniqueEvaluateur(ItemAppreciationDto techniqueEvaluateur) {
		this.techniqueEvaluateur = techniqueEvaluateur;
	}

	public ItemAppreciationDto getTechniqueEvalue() {
		return techniqueEvalue;
	}

	public void setTechniqueEvalue(ItemAppreciationDto techniqueEvalue) {
		this.techniqueEvalue = techniqueEvalue;
	}
}
