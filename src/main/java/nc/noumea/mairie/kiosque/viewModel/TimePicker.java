package nc.noumea.mairie.kiosque.viewModel;

import java.util.ArrayList;
import java.util.List;

public class TimePicker {

	private List<String> listeMinutes;

	private List<String> listeHeures;

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

}
