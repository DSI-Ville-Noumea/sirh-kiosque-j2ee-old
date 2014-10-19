package nc.noumea.mairie.kiosque.eae.dto;

import java.util.Date;

public class EaeIdentificationStatutDto {

	private Integer ancienneteEchelonJours;
	private String cadre;
	private String categorie;
	private String classification;
	private Date dateEffet;
	private String echelon;
	private String grade;
	private String nouvGrade;
	private String nouvEchelon;
	private String statutPrecision;

	private EaeAgentPositionAdministrativeDto statut;

	public EaeAgentPositionAdministrativeDto getStatut() {
		return statut;
	}

	public void setStatut(EaeAgentPositionAdministrativeDto statut) {
		this.statut = statut;
	}

	public String getStatutPrecision() {
		return statutPrecision;
	}

	public void setStatutPrecision(String statutPrecision) {
		this.statutPrecision = statutPrecision;
	}

	public String getCadre() {
		return cadre;
	}

	public void setCadre(String cadre) {
		this.cadre = cadre;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Integer getAncienneteEchelonJours() {
		return ancienneteEchelonJours;
	}

	public void setAncienneteEchelonJours(Integer ancienneteEchelonJours) {
		this.ancienneteEchelonJours = ancienneteEchelonJours;
	}

	public String getNouvGrade() {
		return nouvGrade;
	}

	public void setNouvGrade(String nouvGrade) {
		this.nouvGrade = nouvGrade;
	}

	public String getNouvEchelon() {
		return nouvEchelon;
	}

	public void setNouvEchelon(String nouvEchelon) {
		this.nouvEchelon = nouvEchelon;
	}

	public Date getDateEffet() {
		return dateEffet;
	}

	public void setDateEffet(Date dateEffet) {
		this.dateEffet = dateEffet;
	}
}
