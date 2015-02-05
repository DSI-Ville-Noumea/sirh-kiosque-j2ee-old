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
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;

public class FichePointageDtoKiosque {

	private Date dateLundi;
	private AgentWithServiceDto agent;
	private String semaine;
	private List<JourPointageDtoKiosque> saisies;
	private boolean isDPM;
	private boolean isINASuperieur315;

	public Date getDateLundi() {
		return dateLundi;
	}

	public void setDateLundi(Date dateLundi) {
		this.dateLundi = dateLundi;
	}

	public String getSemaine() {
		return semaine;
	}

	public void setSemaine(String semaine) {
		this.semaine = semaine;
	}

	public AgentWithServiceDto getAgent() {
		return agent;
	}

	public void setAgent(AgentWithServiceDto agent) {
		this.agent = agent;
	}

	public List<JourPointageDtoKiosque> getSaisies() {
		return saisies;
	}

	public void setSaisies(List<JourPointageDtoKiosque> saisies) {
		this.saisies = saisies;
	}

	public boolean isDPM() {
		return isDPM;
	}

	public void setDPM(boolean isDPM) {
		this.isDPM = isDPM;
	}

	public boolean isINASuperieur315() {
		return isINASuperieur315;
	}

	public void setINASuperieur315(boolean isINASuperieur315) {
		this.isINASuperieur315 = isINASuperieur315;
	}
}
