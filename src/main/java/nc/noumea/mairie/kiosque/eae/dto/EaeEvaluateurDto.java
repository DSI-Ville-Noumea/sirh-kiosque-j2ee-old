package nc.noumea.mairie.kiosque.eae.dto;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Mairie de Nouméa
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

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

public class EaeEvaluateurDto {

	private Integer		idEaeEvaluateur;
	private int			idAgent;
	private String		fonction;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeService;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeCollectivite;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeFonction;
	private AgentDto	agent;

	public Integer getIdEaeEvaluateur() {
		return idEaeEvaluateur;
	}

	public void setIdEaeEvaluateur(Integer idEaeEvaluateur) {
		this.idEaeEvaluateur = idEaeEvaluateur;
	}

	public int getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(int idAgent) {
		this.idAgent = idAgent;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public Date getDateEntreeService() {
		return dateEntreeService;
	}

	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
	}

	public Date getDateEntreeCollectivite() {
		return dateEntreeCollectivite;
	}

	public void setDateEntreeCollectivite(Date dateEntreeCollectivite) {
		this.dateEntreeCollectivite = dateEntreeCollectivite;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public AgentDto getAgent() {
		return agent;
	}

	public void setAgent(AgentDto agent) {
		this.agent = agent;
	}
}
