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


public class AgentDto implements Comparable<AgentDto> {

	private String nom;
	private String prenom;
	private Integer idAgent;
	private String civilite;

	// Pour la gestion des droits des absences
	private boolean selectedDroitAbs;

	public AgentDto() {

	}

	public AgentDto(AgentWithServiceDto agDto) {
		this.nom = agDto.getNom();
		this.prenom = agDto.getPrenom();
		this.idAgent = agDto.getIdAgent();
		this.civilite = agDto.getCivilite();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return idAgent.equals(((AgentDto) obj).getIdAgent());
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public void setSelectedDroitAbs(boolean contains) {
		this.selectedDroitAbs = contains;
	}

	public boolean isSelectedDroitAbs() {
		return selectedDroitAbs;
	}

	@Override
	public int compareTo(AgentDto arg0) {
		return this.nom.compareTo(arg0.nom);
	}
}
