package nc.noumea.mairie.kiosque.abs.dto;

import java.io.Serializable;

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


public class RefGroupeAbsenceDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8143811354330199233L;
	
	private Integer idRefGroupeAbsence;
	private String code;
	private String libelle;

	public RefGroupeAbsenceDto() {
	}

	public Integer getIdRefGroupeAbsence() {
		return idRefGroupeAbsence;
	}

	public void setIdRefGroupeAbsence(Integer idRefGroupeAbsence) {
		this.idRefGroupeAbsence = idRefGroupeAbsence;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

}
