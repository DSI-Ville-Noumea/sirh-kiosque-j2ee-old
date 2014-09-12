package nc.noumea.mairie.kiosque.ptg.dto;

public class PrimeDto extends PointageDto {

	private String titre;
	private String typeSaisie;
	private Integer quantite;
	private Integer numRubrique;
	private Integer idRefPrime;

	/* POUR L'AFFICHAGE */
	public boolean isPeriodeHeure() {
		return typeSaisie.equals("PERIODE_HEURES");
	}

	public boolean isnbHeures() {
		return typeSaisie.equals("NB_HEURES");
	}

	public boolean isnbIndemnites() {
		return typeSaisie.equals("NB_INDEMNITES");
	}

	public boolean isCaseACocher() {
		return typeSaisie.equals("CASE_A_COCHER");
	}

	public boolean getCheckCoche() {
		return quantite != null;
	}

	public String getLabelCoche() {
		return quantite == null ? "Non" : "Oui";
	}

	public String getEtatToString() {
		if (getIdRefEtat() == null)
			return "Non saisi";
		return EtatPointageEnum.getEtatPointageEnum(getIdRefEtat()).name();
	}

	/* FIN DE POUR L'AFFICHAGE */

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTypeSaisie() {
		return typeSaisie;
	}

	public void setTypeSaisie(String typeSaisie) {
		this.typeSaisie = typeSaisie;
	}

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

	public Integer getNumRubrique() {
		return numRubrique;
	}

	public void setNumRubrique(Integer numRubrique) {
		this.numRubrique = numRubrique;
	}

	public Integer getIdRefPrime() {
		return idRefPrime;
	}

	public void setIdRefPrime(Integer idRefPrime) {
		this.idRefPrime = idRefPrime;
	}

}
