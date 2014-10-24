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
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentEaeDto;

public class EaeIdentificationDto {

	private AgentEaeDto agent;
	private Date dateEntretien;
	private List<String> diplomes;
	private List<String> formations;
	private List<String> parcoursPros;
	private List<AgentEaeDto> evaluateurs;
	private int idEae;
	private EaeListeDto position;
	private EaeIdentificationSituationDto situation;
	private EaeIdentificationStatutDto statut;

	public EaeIdentificationDto() {
		evaluateurs = new ArrayList<AgentEaeDto>();
		diplomes = new ArrayList<String>();
		parcoursPros = new ArrayList<String>();
		formations = new ArrayList<String>();
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public List<AgentEaeDto> getEvaluateurs() {
		return evaluateurs;
	}

	public void setEvaluateurs(List<AgentEaeDto> evaluateurs) {
		this.evaluateurs = evaluateurs;
	}

	public AgentEaeDto getAgent() {
		return agent;
	}

	public void setAgent(AgentEaeDto agent) {
		this.agent = agent;
	}

	public List<String> getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(List<String> diplomes) {
		this.diplomes = diplomes;
	}

	public List<String> getParcoursPros() {
		return parcoursPros;
	}

	public void setParcoursPros(List<String> parcoursPros) {
		this.parcoursPros = parcoursPros;
	}

	public List<String> getFormations() {
		return formations;
	}

	public void setFormations(List<String> formations) {
		this.formations = formations;
	}

	public EaeIdentificationSituationDto getSituation() {
		return situation;
	}

	public void setSituation(EaeIdentificationSituationDto situation) {
		this.situation = situation;
	}

	public EaeIdentificationStatutDto getStatut() {
		return statut;
	}

	public void setStatut(EaeIdentificationStatutDto statut) {
		this.statut = statut;
	}

	public EaeListeDto getPosition() {
		return position;
	}

	public void setPosition(EaeListeDto position) {
		this.position = position;
	}
}
