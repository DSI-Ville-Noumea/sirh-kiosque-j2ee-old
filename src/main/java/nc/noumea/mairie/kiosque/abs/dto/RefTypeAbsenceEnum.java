package nc.noumea.mairie.kiosque.abs.dto;

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

	CONGE_ANNUEL(1), REPOS_COMP(2), RECUP(3), MALADIES(6), ASA_A48(7), ASA_A54(8), ASA_A55(9), ASA_A52(10), ASA_A53(
			11), ASA_A49(12), ASA_A50(13);

	private int type;

	private RefTypeAbsenceEnum(int _type) {
		type = _type;
	}

	public int getValue() {
		return type;
	}

	public static RefTypeAbsenceEnum getRefTypeAbsenceEnum(Integer type) {

		if (type == null)
			return null;

		switch (type) {
			case 1:
				return CONGE_ANNUEL;
			case 2:
				return REPOS_COMP;
			case 3:
				return RECUP;
			case 6:
				return MALADIES;
			case 7:
				return ASA_A48;
			case 8:
				return ASA_A54;
			case 9:
				return ASA_A55;
			case 10:
				return ASA_A52;
			case 11:
				return ASA_A53;
			case 12:
				return ASA_A49;
			case 13:
				return ASA_A50;
			default:
				return null;
		}
	}

	public static String getLibelleRefTypeAbsenceEnum(Integer type) {

		if (type == null)
			return "";

		switch (type) {
			case 1:
				return "Congés annuels";
			case 2:
				return "Repos compensateur";
			case 3:
				return "Récupérations";
			case 6:
				return "Maladies";
			case 7:
				return "Réunion des membres du bureau directeur";
			case 8:
				return "Congrès et conseil syndical";
			case 9:
				return "Délégation DP";
			case 10:
				return "Décharge de service CTP";
			case 11:
				return "Formation syndicale";
			case 12:
				return "Participation à une réunion syndicale";
			case 13:
				return "Activité institutionnelle";
			default:
				return "";
		}
	}
}
