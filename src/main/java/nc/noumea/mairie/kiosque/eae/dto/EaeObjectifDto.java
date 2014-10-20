package nc.noumea.mairie.kiosque.eae.dto;

public class EaeObjectifDto {

	private String commentaire;
	private Integer idEaeResultat;
	private String objectif;
	private String resultat;

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getIdEaeResultat() {
		return idEaeResultat;
	}

	public void setIdEaeResultat(Integer idEaeResultat) {
		this.idEaeResultat = idEaeResultat;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

}
