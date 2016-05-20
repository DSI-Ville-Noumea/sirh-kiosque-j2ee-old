package nc.noumea.mairie.kiosque.viewModel;

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


import java.util.ArrayList;
import java.util.List;

public class TimePicker {

	private List<String> listeMinutes;

	private List<String> listeHeures;

	private List<String> listeMinutesPointage;

	private List<String> listeHeuresPointage;

	private List<String> listeHeuresEaeDureeEntretien;

	public List<String> getListeMinutes() {
		listeMinutes = new ArrayList<>();
		listeMinutes.add("00");
		listeMinutes.add("05");
		listeMinutes.add("10");
		listeMinutes.add("15");
		listeMinutes.add("20");
		listeMinutes.add("25");
		listeMinutes.add("30");
		listeMinutes.add("35");
		listeMinutes.add("40");
		listeMinutes.add("45");
		listeMinutes.add("50");
		listeMinutes.add("55");

		return listeMinutes;
	}

	public void setListeMinutes(List<String> listeMinutes) {
		this.listeMinutes = listeMinutes;
	}

	public List<String> getListeHeures() {
		listeHeures = new ArrayList<>();
		listeHeures.add("00");
		listeHeures.add("01");
		listeHeures.add("02");
		listeHeures.add("03");
		listeHeures.add("04");
		listeHeures.add("05");
		listeHeures.add("06");
		listeHeures.add("07");
		listeHeures.add("08");
		listeHeures.add("09");
		listeHeures.add("10");
		listeHeures.add("11");
		listeHeures.add("12");
		listeHeures.add("13");
		listeHeures.add("14");
		listeHeures.add("15");
		listeHeures.add("16");
		listeHeures.add("17");
		listeHeures.add("18");
		listeHeures.add("19");
		listeHeures.add("20");
		listeHeures.add("21");
		listeHeures.add("22");
		listeHeures.add("23");
		listeHeures.add("24");

		return listeHeures;
	}

	public void setListeHeures(List<String> listeHeures) {
		this.listeHeures = listeHeures;
	}

	public List<String> getListeMinutesPointage() {
		listeMinutesPointage = new ArrayList<>();
		listeMinutesPointage.add("00");
		listeMinutesPointage.add("15");
		listeMinutesPointage.add("30");
		listeMinutesPointage.add("45");
		// evol #19796
		listeMinutesPointage.add("48");
		return listeMinutesPointage;
	}

	public void setListeMinutesPointage(List<String> listeMinutesPointage) {
		this.listeMinutesPointage = listeMinutesPointage;
	}

	public List<String> getListeHeuresPointage() {
		listeHeuresPointage = new ArrayList<>();
		listeHeuresPointage.add("00");
		listeHeuresPointage.add("01");
		listeHeuresPointage.add("02");
		listeHeuresPointage.add("03");
		listeHeuresPointage.add("04");
		listeHeuresPointage.add("05");
		listeHeuresPointage.add("06");
		listeHeuresPointage.add("07");
		listeHeuresPointage.add("08");
		listeHeuresPointage.add("09");
		listeHeuresPointage.add("10");
		listeHeuresPointage.add("11");
		listeHeuresPointage.add("12");
		listeHeuresPointage.add("13");
		listeHeuresPointage.add("14");
		listeHeuresPointage.add("15");
		listeHeuresPointage.add("16");
		listeHeuresPointage.add("17");
		listeHeuresPointage.add("18");
		listeHeuresPointage.add("19");
		listeHeuresPointage.add("20");
		listeHeuresPointage.add("21");
		listeHeuresPointage.add("22");
		listeHeuresPointage.add("23");
		return listeHeuresPointage;
	}

	public void setListeHeuresPointage(List<String> listeHeuresPointage) {
		this.listeHeuresPointage = listeHeuresPointage;
	}

	public List<String> getListeHeuresEaeDureeEntretien() {
		listeHeuresEaeDureeEntretien = new ArrayList<>();
		listeHeuresEaeDureeEntretien.add("00");
		listeHeuresEaeDureeEntretien.add("01");
		listeHeuresEaeDureeEntretien.add("02");
		listeHeuresEaeDureeEntretien.add("03");
		listeHeuresEaeDureeEntretien.add("04");
		listeHeuresEaeDureeEntretien.add("05");
		listeHeuresEaeDureeEntretien.add("06");
		listeHeuresEaeDureeEntretien.add("07");
		listeHeuresEaeDureeEntretien.add("08");
		listeHeuresEaeDureeEntretien.add("09");
		listeHeuresEaeDureeEntretien.add("10");
		listeHeuresEaeDureeEntretien.add("11");
		listeHeuresEaeDureeEntretien.add("12");

		return listeHeuresEaeDureeEntretien;
	}

	public void setListeHeuresEaeDureeEntretien(List<String> listeHeuresEaeDureeEntretien) {
		this.listeHeuresEaeDureeEntretien = listeHeuresEaeDureeEntretien;
	}

}
