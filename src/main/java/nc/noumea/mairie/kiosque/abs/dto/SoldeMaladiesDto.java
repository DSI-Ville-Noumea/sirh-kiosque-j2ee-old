package nc.noumea.mairie.kiosque.abs.dto;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Mairie de Nouméa
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

import java.io.Serializable;

public class SoldeMaladiesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5431411920902425859L;

	// droits en jours en plein salaire
	private Integer droitsPleinSalaire;
	// droits en jours en demi salaire
	private Integer droitsDemiSalaire;
	// reste a prendre en jours en plein salaire
	private Integer rapPleinSalaire;
	// reste a prendre en jours en demi salaire
	private Integer rapDemiSalaire;
	// nombre jours totaux pris
	private Integer totalPris;
	
	public Integer getDroitsPleinSalaire() {
		return droitsPleinSalaire;
	}
	public void setDroitsPleinSalaire(Integer droitsPleinSalaire) {
		this.droitsPleinSalaire = droitsPleinSalaire;
	}
	public Integer getDroitsDemiSalaire() {
		return droitsDemiSalaire;
	}
	public void setDroitsDemiSalaire(Integer droitsDemiSalaire) {
		this.droitsDemiSalaire = droitsDemiSalaire;
	}
	public Integer getTotalPris() {
		return totalPris;
	}
	public void setTotalPris(Integer totalPris) {
		this.totalPris = totalPris;
	}
	public Integer getRapPleinSalaire() {
		return rapPleinSalaire;
	}
	public void setRapPleinSalaire(Integer rapPleinSalaire) {
		this.rapPleinSalaire = rapPleinSalaire;
	}
	public Integer getRapDemiSalaire() {
		return rapDemiSalaire;
	}
	public void setRapDemiSalaire(Integer rapDemiSalaire) {
		this.rapDemiSalaire = rapDemiSalaire;
	}
	
}
