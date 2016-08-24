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


import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;

public class EaeFinalizationInformationDto {

	private int				idEae;
	private int				annee;

	private AgentDto		agentEvalue;
	private AgentDto		agentDelegataire;
	private List<AgentDto>	agentsEvaluateurs;
	private List<AgentDto>	agentsShd;
	private Double			noteAnnee;
	private String			commentaire;
	private byte[]			bFile;
	private String			nameFile;
	private String			typeFile;

	public EaeFinalizationInformationDto() {
		agentsEvaluateurs = new ArrayList<AgentDto>();
		agentsShd = new ArrayList<AgentDto>();
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public AgentDto getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(AgentDto agentEvalue) {
		this.agentEvalue = agentEvalue;
	}

	public AgentDto getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(AgentDto agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}

	public List<AgentDto> getAgentsEvaluateurs() {
		return agentsEvaluateurs;
	}

	public void setAgentsEvaluateurs(List<AgentDto> agentsEvaluateurs) {
		this.agentsEvaluateurs = agentsEvaluateurs;
	}

	public List<AgentDto> getAgentsShd() {
		return agentsShd;
	}

	public void setAgentsShd(List<AgentDto> agentsShd) {
		this.agentsShd = agentsShd;
	}

	public Double getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Double noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public byte[] getbFile() {
		return bFile;
	}

	public void setbFile(byte[] bFile) {
		this.bFile = bFile;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getTypeFile() {
		return typeFile;
	}

	public void setTypeFile(String typeFile) {
		this.typeFile = typeFile;
	}

}
