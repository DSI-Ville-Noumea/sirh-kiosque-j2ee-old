package nc.noumea.mairie.kiosque.dto;

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

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class BirtDto {

	private AgentEaeDto	agent;
	private Integer		ancienneteEchelonJours;
	private Integer		avctDureeMax;
	private Integer		avctDureeMin;
	private Integer		avctDureeMoy;
	private String		cadre;
	private String		categorie;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEffetAvancement;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeAdministration;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeCollectivite;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeFonctionnaire;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeService;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeFonction;
	private String		echelon;
	private boolean		estDetache;
	private boolean		estEncadrant;
	private String		grade;
	private Integer		idAgent;
	private Integer		idEaeEvalue;
	private String		position;
	private String		statut;
	private String		fonction;
	private Integer		idEaeEvaluateur;

	public BirtDto() {

	}

	public AgentEaeDto getAgent() {
		return agent;
	}

	public void setAgent(AgentEaeDto agent) {
		this.agent = agent;
	}

	public Integer getAncienneteEchelonJours() {
		return ancienneteEchelonJours;
	}

	public void setAncienneteEchelonJours(Integer ancienneteEchelonJours) {
		this.ancienneteEchelonJours = ancienneteEchelonJours;
	}

	public Integer getAvctDureeMax() {
		return avctDureeMax;
	}

	public void setAvctDureeMax(Integer avctDureeMax) {
		this.avctDureeMax = avctDureeMax;
	}

	public Integer getAvctDureeMin() {
		return avctDureeMin;
	}

	public void setAvctDureeMin(Integer avctDureeMin) {
		this.avctDureeMin = avctDureeMin;
	}

	public Integer getAvctDureeMoy() {
		return avctDureeMoy;
	}

	public void setAvctDureeMoy(Integer avctDureeMoy) {
		this.avctDureeMoy = avctDureeMoy;
	}

	public String getCadre() {
		return cadre;
	}

	public void setCadre(String cadre) {
		this.cadre = cadre;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public Date getDateEffetAvancement() {
		return dateEffetAvancement;
	}

	public void setDateEffetAvancement(Date dateEffetAvancement) {
		this.dateEffetAvancement = dateEffetAvancement;
	}

	public Date getDateEntreeAdministration() {
		return dateEntreeAdministration;
	}

	public void setDateEntreeAdministration(Date dateEntreeAdministration) {
		this.dateEntreeAdministration = dateEntreeAdministration;
	}

	public Date getDateEntreeCollectivite() {
		return dateEntreeCollectivite;
	}

	public void setDateEntreeCollectivite(Date dateEntreeCollectivite) {
		this.dateEntreeCollectivite = dateEntreeCollectivite;
	}

	public Date getDateEntreeFonctionnaire() {
		return dateEntreeFonctionnaire;
	}

	public void setDateEntreeFonctionnaire(Date dateEntreeFonctionnaire) {
		this.dateEntreeFonctionnaire = dateEntreeFonctionnaire;
	}

	public Date getDateEntreeService() {
		return dateEntreeService;
	}

	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public boolean isEstDetache() {
		return estDetache;
	}

	public void setEstDetache(boolean estDetache) {
		this.estDetache = estDetache;
	}

	public boolean isEstEncadrant() {
		return estEncadrant;
	}

	public void setEstEncadrant(boolean estEncadrant) {
		this.estEncadrant = estEncadrant;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Integer getIdEaeEvalue() {
		return idEaeEvalue;
	}

	public void setIdEaeEvalue(Integer idEaeEvalue) {
		this.idEaeEvalue = idEaeEvalue;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public Integer getIdEaeEvaluateur() {
		return idEaeEvaluateur;
	}

	public void setIdEaeEvaluateur(Integer idEaeEvaluateur) {
		this.idEaeEvaluateur = idEaeEvaluateur;
	}
}
