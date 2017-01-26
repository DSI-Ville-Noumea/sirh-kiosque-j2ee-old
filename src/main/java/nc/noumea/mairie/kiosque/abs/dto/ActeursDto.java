package nc.noumea.mairie.kiosque.abs.dto;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
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

public class ActeursDto {

	private List<AgentDto> listOperateurs;
	
	private List<AgentDto> listViseurs;
	
	private List<ApprobateurDto> listApprobateurs;

	public ActeursDto() {
		this.listOperateurs = new ArrayList<AgentDto>();
		this.listViseurs = new ArrayList<AgentDto>();
		this.listApprobateurs = new ArrayList<ApprobateurDto>();
	}
	
	public List<AgentDto> getListOperateurs() {
		return listOperateurs;
	}

	public void setListOperateurs(List<AgentDto> listOperateurs) {
		this.listOperateurs = listOperateurs;
	}

	public List<AgentDto> getListViseurs() {
		return listViseurs;
	}

	public void setListViseurs(List<AgentDto> listViseurs) {
		this.listViseurs = listViseurs;
	}

	public List<ApprobateurDto> getListApprobateurs() {
		return listApprobateurs;
	}

	public void setListApprobateurs(List<ApprobateurDto> listApprobateurs) {
		this.listApprobateurs = listApprobateurs;
	}
	
}
