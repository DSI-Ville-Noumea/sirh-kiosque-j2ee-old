package nc.noumea.mairie.kiosque.eae.dto;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

public class EaeFichePosteDto {

	private int				idEae;
	private String			intitule;
	private String			grade;
	private String			emploi;
	private String			directionService;
	private String			sectionService;
	private String			service;
	private String			localisation;
	private String			missions;
	private String			responsableNom;
	private String			responsablePrenom;
	private String			responsableFonction;
	private List<String>	activites;
	private List<String>	competencesSavoir;
	private List<String>	competencesSavoirFaire;
	private List<String>	competencesComportementProfessionnel;

	private Integer			idAgentShd;

	public EaeFichePosteDto() {
		activites = new ArrayList<String>();
		competencesComportementProfessionnel = new ArrayList<String>();
		competencesSavoir = new ArrayList<String>();
		competencesSavoirFaire = new ArrayList<String>();
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEmploi() {
		return emploi;
	}

	public void setEmploi(String emploi) {
		this.emploi = emploi;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getSectionService() {
		return sectionService;
	}

	public void setSectionService(String sectionService) {
		this.sectionService = sectionService;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public String getMissions() {
		return missions;
	}

	public void setMissions(String missions) {
		this.missions = missions;
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

	public String getResponsableFonction() {
		return responsableFonction;
	}

	public void setResponsableFonction(String responsableFonction) {
		this.responsableFonction = responsableFonction;
	}

	public List<String> getActivites() {
		return activites;
	}

	public void setActivites(List<String> activites) {
		this.activites = activites;
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

	public List<String> getCompetencesComportementProfessionnel() {
		return competencesComportementProfessionnel;
	}

	public void setCompetencesComportementProfessionnel(List<String> competencesComportementProfessionnel) {
		this.competencesComportementProfessionnel = competencesComportementProfessionnel;
	}

	public Integer getIdAgentShd() {
		return idAgentShd;
	}

	public void setIdAgentShd(Integer idAgentShd) {
		this.idAgentShd = idAgentShd;
	}
}
