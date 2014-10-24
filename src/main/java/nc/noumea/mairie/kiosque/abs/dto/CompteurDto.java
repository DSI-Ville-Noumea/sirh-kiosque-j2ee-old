package nc.noumea.mairie.kiosque.abs.dto;

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

public class CompteurDto {

	private Integer idAgent;

	private Double dureeAAjouter;

	private Double dureeARetrancher;

	private Integer idMotifCompteur;

	private boolean isAnneePrecedente;

	private Date dateDebut;

	private Date dateFin;

	private Integer idOrganisationSyndicale;

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Double getDureeAAjouter() {
		return dureeAAjouter;
	}

	public void setDureeAAjouter(Double dureeAAjouter) {
		this.dureeAAjouter = dureeAAjouter;
	}

	public Double getDureeARetrancher() {
		return dureeARetrancher;
	}

	public void setDureeARetrancher(Double dureeARetrancher) {
		this.dureeARetrancher = dureeARetrancher;
	}

	public Integer getIdMotifCompteur() {
		return idMotifCompteur;
	}

	public void setIdMotifCompteur(Integer idMotifCompteur) {
		this.idMotifCompteur = idMotifCompteur;
	}

	public boolean isAnneePrecedente() {
		return isAnneePrecedente;
	}

	public void setAnneePrecedente(boolean isAnneePrecedente) {
		this.isAnneePrecedente = isAnneePrecedente;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Integer getIdOrganisationSyndicale() {
		return idOrganisationSyndicale;
	}

	public void setIdOrganisationSyndicale(Integer idOrganisationSyndicale) {
		this.idOrganisationSyndicale = idOrganisationSyndicale;
	}

}
