package nc.noumea.mairie.kiosque.abs.dto;

public class MotifCompteurDto {

	private Integer idMotifCompteur;
	private String libelle;
	private Integer idRefTypeAbsence;

	public MotifCompteurDto() {
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getIdMotifCompteur() {
		return idMotifCompteur;
	}

	public void setIdMotifCompteur(Integer idMotifCompteur) {
		this.idMotifCompteur = idMotifCompteur;
	}

	public Integer getIdRefTypeAbsence() {
		return idRefTypeAbsence;
	}

	public void setIdRefTypeAbsence(Integer idRefTypeAbsence) {
		this.idRefTypeAbsence = idRefTypeAbsence;
	}
}
