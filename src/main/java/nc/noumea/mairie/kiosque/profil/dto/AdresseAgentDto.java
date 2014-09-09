package nc.noumea.mairie.kiosque.profil.dto;

public class AdresseAgentDto {

	/*
	 * POUR LES PROJETS KIOSQUE J2EE
	 */

	private String bp;
	private String adresseComplementaire;
	private String numRue;
	private String bisTer;
	private String rue;
	private String villeDomicile;
	private Integer codePostalDomicile;
	private String villeBp;
	private Integer codePostalBp;

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public String getAdresseComplementaire() {
		return adresseComplementaire;
	}

	public void setAdresseComplementaire(String adresseComplementaire) {
		this.adresseComplementaire = adresseComplementaire;
	}

	public String getNumRue() {
		return numRue;
	}

	public void setNumRue(String numRue) {
		this.numRue = numRue;
	}

	public String getBisTer() {
		return bisTer;
	}

	public void setBisTer(String bisTer) {
		this.bisTer = bisTer;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getVilleDomicile() {
		return villeDomicile;
	}

	public void setVilleDomicile(String villeDomicile) {
		this.villeDomicile = villeDomicile;
	}

	public Integer getCodePostalDomicile() {
		return codePostalDomicile;
	}

	public void setCodePostalDomicile(Integer codePostalDomicile) {
		this.codePostalDomicile = codePostalDomicile;
	}

	public String getVilleBp() {
		return villeBp;
	}

	public void setVilleBp(String villeBp) {
		this.villeBp = villeBp;
	}

	public Integer getCodePostalBp() {
		return codePostalBp;
	}

	public void setCodePostalBp(Integer codePostalBp) {
		this.codePostalBp = codePostalBp;
	}

}
