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


public enum RefTypeGroupeAbsenceEnum {

	RECUP(1), REPOS_COMP(2), AS(3), CONGES_EXCEP(4), CONGES_ANNUELS(5), NOT_EXIST(99);

	private int type;

	private RefTypeGroupeAbsenceEnum(int _type) {
		type = _type;
	}

	public int getValue() {
		return type;
	}

	public static RefTypeGroupeAbsenceEnum getRefTypeGroupeAbsenceEnum(Integer type) {

		if (type == null)
			return null;

		switch (type) {
			case 1:
				return RECUP;
			case 2:
				return REPOS_COMP;
			case 3:
				return AS;
			case 4:
				return CONGES_EXCEP;
			case 5:
				return CONGES_ANNUELS;
			default:
				return NOT_EXIST;
		}
	}
}
