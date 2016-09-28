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


public class AgentEaeDto {

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date	dateDerniereEmbauche;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date	dateNaissance;
	private Integer	idAgent;
	private String	nomMarital;
	private String	nomPatronymique;
	private String	nomUsage;
	private Integer	nomatr;
	private String	prenom;
	private String	prenomUsage;
	public AgentEaeDto() {

	}

	public Date getDateDerniereEmbauche() {
		return dateDerniereEmbauche;
	}

	public void setDateDerniereEmbauche(Date dateDerniereEmbauche) {
		this.dateDerniereEmbauche = dateDerniereEmbauche;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public String getNomMarital() {
		return nomMarital;
	}

	public void setNomMarital(String nomMarital) {
		this.nomMarital = nomMarital;
	}

	public String getNomPatronymique() {
		return nomPatronymique;
	}

	public void setNomPatronymique(String nomPatronymique) {
		this.nomPatronymique = nomPatronymique;
	}

	public String getNomUsage() {
		return nomUsage;
	}

	public void setNomUsage(String nomUsage) {
		this.nomUsage = nomUsage;
	}

	public Integer getNomatr() {
		return nomatr;
	}

	public void setNomatr(Integer nomatr) {
		this.nomatr = nomatr;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPrenomUsage() {
		return prenomUsage;
	}

	public void setPrenomUsage(String prenomUsage) {
		this.prenomUsage = prenomUsage;
	}
}
