package nc.noumea.mairie.kiosque.eae.dto;

import java.util.Date;

public class EaeDeveloppementDto {

	private Integer idEaeDeveloppement;
	private Date echeance;
	private String libelle;
	private Integer priorisation;

	public Integer getIdEaeDeveloppement() {
		return idEaeDeveloppement;
	}

	public void setIdEaeDeveloppement(Integer idEaeDeveloppement) {
		this.idEaeDeveloppement = idEaeDeveloppement;
	}

	public Date getEcheance() {
		return echeance;
	}

	public void setEcheance(Date echeance) {
		this.echeance = echeance;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getPriorisation() {
		return priorisation;
	}

	public void setPriorisation(Integer priorisation) {
		this.priorisation = priorisation;
	}

}
