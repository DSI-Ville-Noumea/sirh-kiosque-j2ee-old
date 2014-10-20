package nc.noumea.mairie.kiosque.eae.dto;

import java.util.ArrayList;
import java.util.List;

public class EaeFichePosteDto {

	private List<String> activites;
	private List<String> competencesComportementProfessionnel;
	private List<String> competencesSavoir;
	private List<String> competencesSavoirFaire;
	private String directionService;
	private String emploi;
	private String grade;
	private String intitule;
	private String localisation;
	private int idEae;
	private String missions;
	private String responsableFonction;
	private String responsableNom;
	private String responsablePrenom;
	private String service;

	public EaeFichePosteDto() {
		activites = new ArrayList<String>();
		competencesComportementProfessionnel = new ArrayList<String>();
		competencesSavoir = new ArrayList<String>();
		competencesSavoirFaire = new ArrayList<String>();
	}

	public List<String> getActivites() {
		return activites;
	}

	public void setActivites(List<String> activites) {
		this.activites = activites;
	}

	public List<String> getCompetencesComportementProfessionnel() {
		return competencesComportementProfessionnel;
	}

	public void setCompetencesComportementProfessionnel(List<String> competencesComportementProfessionnel) {
		this.competencesComportementProfessionnel = competencesComportementProfessionnel;
	}

	public List<String> getCompetencesSavoir() {
		return competencesSavoir;
	}

	public void setCompetencesSavoir(List<String> competencesSavoir) {
		this.competencesSavoir = competencesSavoir;
	}

	public List<String> getCompetencesSavoirFaire() {
		return competencesSavoirFaire;
	}

	public void setCompetencesSavoirFaire(List<String> competencesSavoirFaire) {
		this.competencesSavoirFaire = competencesSavoirFaire;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getEmploi() {
		return emploi;
	}

	public void setEmploi(String emploi) {
		this.emploi = emploi;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String getMissions() {
		return missions;
	}

	public void setMissions(String missions) {
		this.missions = missions;
	}

	public String getResponsableFonction() {
		return responsableFonction;
	}

	public void setResponsableFonction(String responsableFonction) {
		this.responsableFonction = responsableFonction;
	}

	public String getResponsableNom() {
		return responsableNom;
	}

	public void setResponsableNom(String responsableNom) {
		this.responsableNom = responsableNom;
	}

	public String getResponsablePrenom() {
		return responsablePrenom;
	}

	public void setResponsablePrenom(String responsablePrenom) {
		this.responsablePrenom = responsablePrenom;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}
