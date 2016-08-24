package nc.noumea.mairie.kiosque.eae.dto;

public class EaeCommentaireDto {

	private Integer	idEaeCommentaire;
	private String	text;

	public EaeCommentaireDto() {

	}

	public EaeCommentaireDto(String texte) {
		this.text = texte;
	}

	public Integer getIdEaeCommentaire() {
		return idEaeCommentaire;
	}

	public void setIdEaeCommentaire(Integer idEaeCommentaire) {
		this.idEaeCommentaire = idEaeCommentaire;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
