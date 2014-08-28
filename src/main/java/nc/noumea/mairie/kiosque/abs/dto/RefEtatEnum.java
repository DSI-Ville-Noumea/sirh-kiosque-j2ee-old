package nc.noumea.mairie.kiosque.abs.dto;

import java.util.List;

public enum RefEtatEnum {

	PROVISOIRE(0, "Provisoire"), SAISIE(1, "Saisie"), VISEE_FAVORABLE(2, "Visée favorable"), VISEE_DEFAVORABLE(3,
			"Visée défavorable"), APPROUVEE(4, "Approuvée"), REFUSEE(5, "Refusée"), PRISE(6, "Prise"), ANNULEE(7,
			"Annulée"), VALIDEE(8, "Validée"), REJETE(9, "Rejetée"), EN_ATTENTE(10, "En attente");

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
