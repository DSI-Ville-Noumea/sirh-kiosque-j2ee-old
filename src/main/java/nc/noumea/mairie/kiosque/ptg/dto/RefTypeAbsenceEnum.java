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


public enum RefTypeAbsenceEnum {

	CONCERTEE(1, "Abs. concertée"), NON_CONCERTEE(2, "Abs. non concertée"), IMMEDIATE(3, "Abs. immédiate");

	private Integer type;
	private String lib;

	private RefTypeAbsenceEnum(Integer _type, String _lib) {
		type = _type;
		lib = _lib;
	}

	@Override
	public String toString() {
		return this.name();
	}

	public Integer getValue() {
		return type;
	}

	public String getLib() {
		return lib;
	}

	public static RefTypeAbsenceEnum getRefTypeAbsenceEnum(Integer type) {

		if (type == null)
			return null;

		switch (type) {
			case 1:
				return CONCERTEE;
			case 2:
				return NON_CONCERTEE;
			case 3:
				return IMMEDIATE;
			default:
				return null;
		}
	}
}
