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

import java.util.List;

public enum RefEtatEnum {

	PROVISOIRE(0, "Provisoire"), SAISIE(1, "Saisie"), VISEE_FAVORABLE(2, "Visée favorable"), VISEE_DEFAVORABLE(3,
			"Visée défavorable"), APPROUVEE(4, "Approuvée"), REFUSEE(5, "Refusée"), PRISE(6, "Prise"), ANNULEE(7,
			"Annulée"), VALIDEE(8, "Validée"), REJETE(9, "Rejetée"), EN_ATTENTE(10, "En attente"), A_VALIDER(11,
			"A valider");

	private int codeEtat;
	private String libEtat;

	RefEtatEnum(int _value, String _lib) {
		codeEtat = _value;
		libEtat = _lib;
	}

	public int getCodeEtat() {
		return codeEtat;
	}

	public static RefEtatEnum getRefEtatEnum(Integer codeEtat) {

		if (codeEtat == null)
			return null;

		switch (codeEtat) {
			case 0:
				return PROVISOIRE;
			case 1:
				return SAISIE;
			case 2:
				return VISEE_FAVORABLE;
			case 3:
				return VISEE_DEFAVORABLE;
			case 4:
				return APPROUVEE;
			case 5:
				return REFUSEE;
			case 6:
				return PRISE;
			case 7:
				return ANNULEE;
			case 8:
				return VALIDEE;
			case 9:
				return REJETE;
			case 10:
				return EN_ATTENTE;
			case 11:
				return A_VALIDER;
			default:
				return null;
		}
	}

	public static String listToString(List<RefEtatEnum> list) {

		String result = "";
		if (null != list) {
			for (RefEtatEnum e : list) {
				result += e.name() + " ";
			}
		}

		return result;
	}

	public String getLibEtat() {
		return libEtat;
	}

	public void setLibEtat(String libEtat) {
		this.libEtat = libEtat;
	}
}
