package nc.noumea.mairie.kiosque.abs.dto;

public class RefTypeAbsenceDto {

	private Integer idRefTypeAbsence;
	private String libelle;
	private RefGroupeAbsenceDto groupeAbsence;
	private RefTypeSaisiDto typeSaisiDto;

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

	public RefTypeSaisiDto getTypeSaisiDto() {
		return typeSaisiDto;
	}

	public void setTypeSaisiDto(RefTypeSaisiDto typeSaisiDto) {
		this.typeSaisiDto = typeSaisiDto;
	}

	public RefGroupeAbsenceDto getGroupeAbsence() {
		return groupeAbsence;
	}

	public void setGroupeAbsence(RefGroupeAbsenceDto groupeAbsence) {
		this.groupeAbsence = groupeAbsence;
	}

}
