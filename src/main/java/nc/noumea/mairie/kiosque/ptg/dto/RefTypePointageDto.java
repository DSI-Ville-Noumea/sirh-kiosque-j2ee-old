package nc.noumea.mairie.kiosque.ptg.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RefTypePointageDto {

	private Integer idRefTypePointage;
	private String libelle;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getIdRefTypePointage() {
		return idRefTypePointage;
	}

	public void setIdRefTypePointage(Integer idRefTypePointage) {
		this.idRefTypePointage = idRefTypePointage;
	}
}
