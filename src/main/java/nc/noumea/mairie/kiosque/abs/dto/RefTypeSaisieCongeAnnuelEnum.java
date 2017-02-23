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

public enum RefTypeSaisieCongeAnnuelEnum {
	
	A("A", "TOUS") ,	D("D","SVS"), E("E", "MORGUE"), F("F", "POMPIERS"), C("C", "GARDIENS"), S("S", "SAMEDI");

	private String codeBase;
	private String libelle;
	
	private RefTypeSaisieCongeAnnuelEnum(String codeBase, String libelle) {
		this.codeBase = codeBase;
		this.libelle = libelle;
	}

	public String getCodeBase() {
		return codeBase;
	}

	public void setCodeBase(String codeBase) {
		this.codeBase = codeBase;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public static RefTypeSaisieCongeAnnuelEnum getRefTypeSaisieCongeAnnuelEnum(String codeBase) {

		if (codeBase == null)
			return null;

		switch (codeBase) {
			case "A":
				return A;
			case "C":
				return C;
			case "D":
				return D;
			case "E":
				return E;
			case "F":
				return F;
			case "S":
				return S;
			default:
				return null;
		}
	}
}
