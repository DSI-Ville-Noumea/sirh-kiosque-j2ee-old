package nc.noumea.mairie.kiosque.ptg.dto;

public class RefTypeAbsenceDto {

	private Integer idRefTypeAbsence;
	private String libelle;

	public RefTypeAbsenceDto() {
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getIdRefTypeAbsence() {
		return idRefTypeAbsence;
	}

	public void setIdRefTypeAbsence(Integer idRefTypeAbsence) {
		this.idRefTypeAbsence = idRefTypeAbsence;
	}
}
