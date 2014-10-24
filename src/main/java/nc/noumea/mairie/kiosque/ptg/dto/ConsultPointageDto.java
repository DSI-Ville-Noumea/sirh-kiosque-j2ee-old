package nc.noumea.mairie.kiosque.ptg.dto;

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

import nc.noumea.mairie.kiosque.dto.AgentDto;

public class ConsultPointageDto {

	private Integer idPointage;
	private AgentDto agent;
	private String typePointage;
	private Date date;
	private Date debut;
	private Date fin;
	private String quantite;
	private String motif;
	private String commentaire;
	private Integer idRefEtat;
	private Date dateSaisie;
	private AgentDto operateur;
	private boolean heuresSupRecuperees;
	private boolean heuresSupRappelEnService;

	public Integer getIdPointage() {
		return idPointage;
	}

	public void setIdPointage(Integer idPointage) {
		this.idPointage = idPointage;
	}

	public AgentDto getAgent() {
		return agent;
	}

	public void setAgent(AgentDto agent) {
		this.agent = agent;
	}

	public String getTypePointage() {
		return typePointage;
	}

	public void setTypePointage(String typePointage) {
		this.typePointage = typePointage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public String getQuantite() {
		return quantite;
	}

	public void setQuantite(String quantite) {
		this.quantite = quantite;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getIdRefEtat() {
		return idRefEtat;
	}

	public void setIdRefEtat(Integer idRefEtat) {
		this.idRefEtat = idRefEtat;
	}

	public Date getDateSaisie() {
		return dateSaisie;
	}

	public void setDateSaisie(Date dateSaisie) {
		this.dateSaisie = dateSaisie;
	}

	public AgentDto getOperateur() {
		return operateur;
	}

	public void setOperateur(AgentDto operateur) {
		this.operateur = operateur;
	}

	public boolean isHeuresSupRecuperees() {
		return heuresSupRecuperees;
	}

	public void setHeuresSupRecuperees(boolean heuresSupRecuperees) {
		this.heuresSupRecuperees = heuresSupRecuperees;
	}

	public boolean isHeuresSupRappelEnService() {
		return heuresSupRappelEnService;
	}

	public void setHeuresSupRappelEnService(boolean heuresSupRappelEnService) {
		this.heuresSupRappelEnService = heuresSupRappelEnService;
	}

}
