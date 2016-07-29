package nc.noumea.mairie.kiosque.ptg.dto;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class DpmIndemniteAnneeDto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6104807700822860344L;

	private Integer idDpmIndemAnnee;
	private Integer annee;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateDebut;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateFin;
	private List<DpmIndemniteChoixAgentDto> listDpmIndemniteChoixAgentDto;
	
	public DpmIndemniteAnneeDto() {
		listDpmIndemniteChoixAgentDto = new ArrayList<DpmIndemniteChoixAgentDto>();
	}
	
	public void addDpmIndemniteChoixAgentDto(DpmIndemniteChoixAgentDto choixAgentDto) {
		if(null != getListDpmIndemniteChoixAgentDto())
			getListDpmIndemniteChoixAgentDto().add(choixAgentDto);
	}

	public Integer getIdDpmIndemAnnee() {
		return idDpmIndemAnnee;
	}

	public void setIdDpmIndemAnnee(Integer idDpmIndemAnnee) {
		this.idDpmIndemAnnee = idDpmIndemAnnee;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
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

	public List<DpmIndemniteChoixAgentDto> getListDpmIndemniteChoixAgentDto() {
		return listDpmIndemniteChoixAgentDto;
	}

	public void setListDpmIndemniteChoixAgentDto(List<DpmIndemniteChoixAgentDto> listDpmIndemniteChoixAgentDto) {
		this.listDpmIndemniteChoixAgentDto = listDpmIndemniteChoixAgentDto;
	}

	@Override
	public String toString() {
		return "DpmIndemniteAnneeDto [idDpmIndemAnnee=" + idDpmIndemAnnee + ", annee=" + annee + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin
				+ ", listDpmIndemniteChoixAgentDto=" + listDpmIndemniteChoixAgentDto + "]";
	}
	
}
