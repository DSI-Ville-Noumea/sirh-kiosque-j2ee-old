package nc.noumea.mairie.kiosque.ptg.dto;

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
