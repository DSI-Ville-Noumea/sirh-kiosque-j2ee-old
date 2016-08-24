package nc.noumea.mairie.kiosque.eae.dto;

public class EaeResultatDto {

	private EaeCommentaireDto	commentaire;
	private Integer				idEaeResultat;
	private String				objectif;
	private String				resultat;
	private EaeTypeObjectifDto	typeObjectif;

	public EaeCommentaireDto getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(EaeCommentaireDto commentaire) {
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

	public EaeTypeObjectifDto getTypeObjectif() {
		return typeObjectif;
	}

	public void setTypeObjectif(EaeTypeObjectifDto typeObjectif) {
		this.typeObjectif = typeObjectif;
	}
}
