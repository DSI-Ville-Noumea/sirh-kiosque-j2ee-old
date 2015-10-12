package nc.noumea.mairie.kiosque.abs.dto;

import java.io.Serializable;

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


public class UnitePeriodeQuotaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7389559629193160781L;

	private Integer idRefUnitePeriodeQuota;

	private Integer valeur;

	private String unite;

	/**
	 * defini si la periode est par exemple sur : - 12 mois glissant - ou sur
	 * une année civile
	 */
	private boolean glissant;

	public UnitePeriodeQuotaDto() {
	}

	public Integer getIdRefUnitePeriodeQuota() {
		return idRefUnitePeriodeQuota;
	}

	public void setIdRefUnitePeriodeQuota(Integer idRefUnitePeriodeQuota) {
		this.idRefUnitePeriodeQuota = idRefUnitePeriodeQuota;
	}

	public Integer getValeur() {
		return valeur;
	}

	public void setValeur(Integer valeur) {
		this.valeur = valeur;
	}

	public String getUnite() {
		return unite;
	}

	public void setUnite(String unite) {
		this.unite = unite;
	}

	public boolean isGlissant() {
		return glissant;
	}

	public void setGlissant(boolean glissant) {
		this.glissant = glissant;
	}

}
