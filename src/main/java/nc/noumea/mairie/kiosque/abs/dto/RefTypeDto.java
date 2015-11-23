package nc.noumea.mairie.kiosque.abs.dto;

import java.io.Serializable;

public class RefTypeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3854094040343020224L;
	
	private Integer idRefType;
	private String libelle;
	
	public RefTypeDto() {
	}
	
	public Integer getIdRefType() {
		return idRefType;
	}
	public void setIdRefType(Integer idRefType) {
		this.idRefType = idRefType;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}
