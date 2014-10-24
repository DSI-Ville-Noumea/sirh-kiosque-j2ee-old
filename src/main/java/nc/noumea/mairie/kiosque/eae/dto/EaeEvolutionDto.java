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


import java.util.Date;
import java.util.List;

public class EaeEvolutionDto {

	private int idEae;
	private boolean autrePerspective;
	private boolean changementMetier;
	private boolean concours;
	private boolean mobiliteAutre;
	private boolean mobiliteCollectivite;
	private boolean mobiliteDirection;
	private boolean mobiliteFonctionnelle;
	private boolean mobiliteGeo;
	private boolean mobiliteService;
	private boolean retraite;
	private boolean tempsPartiel;
	private boolean vae;
	private String commentaireEvaluateur;
	private String commentaireEvalue;
	private String commentaireEvolution;
	private Date dateRetraite;
	private EaeListeDto delaiEnvisage;
	private List<EaeDeveloppementDto> developpementCompetences;
	private List<EaeDeveloppementDto> developpementComportement;
	private List<EaeDeveloppementDto> developpementConnaissances;
	private List<EaeDeveloppementDto> developpementExamensConcours;
	private List<EaeDeveloppementDto> developpementFormateur;
	private List<EaeDeveloppementDto> developpementPersonnel;
	private String libelleAutrePerspective;
	private String nomCollectivite;
	private String nomConcours;
	private String nomVae;
	private EaeListeDto pourcentageTempsPartiel;
	private List<EaeSouhaitDto> souhaitsSuggestions;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public boolean getAutrePerspective() {
		return autrePerspective;
	}

	public void setAutrePerspective(boolean autrePerspective) {
		this.autrePerspective = autrePerspective;
	}

	public boolean getConcours() {
		return concours;
	}

	public void setConcours(boolean concours) {
		this.concours = concours;
	}

	public boolean getRetraite() {
		return retraite;
	}

	public void setRetraite(boolean retraite) {
		this.retraite = retraite;
	}

	public boolean getTempsPartiel() {
		return tempsPartiel;
	}

	public void setTempsPartiel(boolean tempsPartiel) {
		this.tempsPartiel = tempsPartiel;
	}

	public boolean getVae() {
		return vae;
	}

	public void setVae(boolean vae) {
		this.vae = vae;
	}

	public String getCommentaireEvaluateur() {
		return commentaireEvaluateur;
	}

	public void setCommentaireEvaluateur(String commentaireEvaluateur) {
		this.commentaireEvaluateur = commentaireEvaluateur;
	}

	public String getCommentaireEvalue() {
		return commentaireEvalue;
	}

	public void setCommentaireEvalue(String commentaireEvalue) {
		this.commentaireEvalue = commentaireEvalue;
	}

	public String getCommentaireEvolution() {
		return commentaireEvolution;
	}

	public void setCommentaireEvolution(String commentaireEvolution) {
		this.commentaireEvolution = commentaireEvolution;
	}

	public Date getDateRetraite() {
		return dateRetraite;
	}

	public void setDateRetraite(Date dateRetraite) {
		this.dateRetraite = dateRetraite;
	}

	public EaeListeDto getDelaiEnvisage() {
		return delaiEnvisage;
	}

	public void setDelaiEnvisage(EaeListeDto delaiEnvisage) {
		this.delaiEnvisage = delaiEnvisage;
	}

	public List<EaeDeveloppementDto> getDeveloppementCompetences() {
		return developpementCompetences;
	}

	public void setDeveloppementCompetences(List<EaeDeveloppementDto> developpementCompetences) {
		this.developpementCompetences = developpementCompetences;
	}

	public List<EaeDeveloppementDto> getDeveloppementComportement() {
		return developpementComportement;
	}

	public void setDeveloppementComportement(List<EaeDeveloppementDto> developpementComportement) {
		this.developpementComportement = developpementComportement;
	}

	public List<EaeDeveloppementDto> getDeveloppementConnaissances() {
		return developpementConnaissances;
	}

	public void setDeveloppementConnaissances(List<EaeDeveloppementDto> developpementConnaissances) {
		this.developpementConnaissances = developpementConnaissances;
	}

	public List<EaeDeveloppementDto> getDeveloppementExamensConcours() {
		return developpementExamensConcours;
	}

	public void setDeveloppementExamensConcours(List<EaeDeveloppementDto> developpementExamensConcours) {
		this.developpementExamensConcours = developpementExamensConcours;
	}

	public List<EaeDeveloppementDto> getDeveloppementFormateur() {
		return developpementFormateur;
	}

	public void setDeveloppementFormateur(List<EaeDeveloppementDto> developpementFormateur) {
		this.developpementFormateur = developpementFormateur;
	}

	public List<EaeDeveloppementDto> getDeveloppementPersonnel() {
		return developpementPersonnel;
	}

	public void setDeveloppementPersonnel(List<EaeDeveloppementDto> developpementPersonnel) {
		this.developpementPersonnel = developpementPersonnel;
	}

	public String getLibelleAutrePerspective() {
		return libelleAutrePerspective;
	}

	public void setLibelleAutrePerspective(String libelleAutrePerspective) {
		this.libelleAutrePerspective = libelleAutrePerspective;
	}

	public String getNomCollectivite() {
		return nomCollectivite;
	}

	public void setNomCollectivite(String nomCollectivite) {
		this.nomCollectivite = nomCollectivite;
	}

	public String getNomConcours() {
		return nomConcours;
	}

	public void setNomConcours(String nomConcours) {
		this.nomConcours = nomConcours;
	}

	public String getNomVae() {
		return nomVae;
	}

	public void setNomVae(String nomVae) {
		this.nomVae = nomVae;
	}

	public EaeListeDto getPourcentageTempsPartiel() {
		return pourcentageTempsPartiel;
	}

	public void setPourcentageTempsPartiel(EaeListeDto pourcentageTempsPartiel) {
		this.pourcentageTempsPartiel = pourcentageTempsPartiel;
	}

	public List<EaeSouhaitDto> getSouhaitsSuggestions() {
		return souhaitsSuggestions;
	}

	public void setSouhaitsSuggestions(List<EaeSouhaitDto> souhaitsSuggestions) {
		this.souhaitsSuggestions = souhaitsSuggestions;
	}

	public boolean isChangementMetier() {
		return changementMetier;
	}

	public boolean isMobiliteAutre() {
		return mobiliteAutre;
	}

	public boolean isMobiliteCollectivite() {
		return mobiliteCollectivite;
	}

	public boolean isMobiliteDirection() {
		return mobiliteDirection;
	}

	public boolean isMobiliteFonctionnelle() {
		return mobiliteFonctionnelle;
	}

	public boolean isMobiliteGeo() {
		return mobiliteGeo;
	}

	public boolean isMobiliteService() {
		return mobiliteService;
	}

	public void setChangementMetier(boolean changementMetier) {
		this.changementMetier = changementMetier;
	}

	public void setMobiliteAutre(boolean mobiliteAutre) {
		this.mobiliteAutre = mobiliteAutre;
	}

	public void setMobiliteCollectivite(boolean mobiliteCollectivite) {
		this.mobiliteCollectivite = mobiliteCollectivite;
	}

	public void setMobiliteDirection(boolean mobiliteDirection) {
		this.mobiliteDirection = mobiliteDirection;
	}

	public void setMobiliteFonctionnelle(boolean mobiliteFonctionnelle) {
		this.mobiliteFonctionnelle = mobiliteFonctionnelle;
	}

	public void setMobiliteGeo(boolean mobiliteGeo) {
		this.mobiliteGeo = mobiliteGeo;
	}

	public void setMobiliteService(boolean mobiliteService) {
		this.mobiliteService = mobiliteService;
	}

}
