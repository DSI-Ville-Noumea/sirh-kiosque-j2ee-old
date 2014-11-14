package nc.noumea.mairie.kiosque.ptg.dto;

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
