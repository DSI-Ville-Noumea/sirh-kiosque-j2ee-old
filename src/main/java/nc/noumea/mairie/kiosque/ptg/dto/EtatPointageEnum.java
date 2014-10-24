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


public enum EtatPointageEnum {

	SAISI(0, "Saisi"), APPROUVE(1, "Approuvé"), REFUSE(2, "Refusé"), REFUSE_DEFINITIVEMENT(3, "Refusé définitivement"), VENTILE(
			4, "Ventilé"), REJETE(5, "Rejeté"), REJETE_DEFINITIVEMENT(6, "Rejeté définitivement"), VALIDE(7, "Validé"), EN_ATTENTE(
			8, "En attente"), JOURNALISE(9, "Journalisé");

	private int codeEtat;
	private String libEtat;

	EtatPointageEnum(int _value, String _lib) {
		codeEtat = _value;
		libEtat = _lib;
	}

	public int getCodeEtat() {
		return codeEtat;
	}

	@Override
	public String toString() {
		return String.valueOf(codeEtat);
	}

	public static EtatPointageEnum getEtatPointageEnum(Integer codeEtat) {

		if (codeEtat == null)
			return null;

		switch (codeEtat) {
			case 0:
				return SAISI;
			case 1:
				return APPROUVE;
			case 2:
				return REFUSE;
			case 3:
				return REFUSE_DEFINITIVEMENT;
			case 4:
				return VENTILE;
			case 5:
				return REJETE;
			case 6:
				return REJETE_DEFINITIVEMENT;
			case 7:
				return VALIDE;
			case 8:
				return EN_ATTENTE;
			case 9:
				return JOURNALISE;
			default:
				return null;
		}
	}

	public String getLibEtat() {
		return libEtat;
	}

	public void setLibEtat(String libEtat) {
		this.libEtat = libEtat;
	}
}
