package nc.noumea.mairie.kiosque.profil.dto;

public class CompteDto {

	private Integer codeBanque;
	private Integer codeGuichet;
	private String numCompte;
	private Integer rib;
	private String libelleBanque;
	private String intituleCompte;

	public Integer getCodeBanque() {
		return codeBanque;
	}

	public void setCodeBanque(Integer codeBanque) {
		this.codeBanque = codeBanque;
	}

	public Integer getCodeGuichet() {
		return codeGuichet;
	}

	public void setCodeGuichet(Integer codeGuichet) {
		this.codeGuichet = codeGuichet;
	}

	public String getNumCompte() {
		return numCompte;
	}

	public void setNumCompte(String numCompte) {
		this.numCompte = numCompte;
	}

	public Integer getRib() {
		return rib;
	}

	public void setRib(Integer rib) {
		this.rib = rib;
	}

	public String getLibelleBanque() {
		return libelleBanque;
	}

	public void setLibelleBanque(String libelleBanque) {
		this.libelleBanque = libelleBanque;
	}

	public String getIntituleCompte() {
		return intituleCompte;
	}

	public void setIntituleCompte(String intituleCompte) {
		this.intituleCompte = intituleCompte;
	}
}
