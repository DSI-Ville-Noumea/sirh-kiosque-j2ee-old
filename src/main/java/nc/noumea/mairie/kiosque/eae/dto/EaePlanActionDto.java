package nc.noumea.mairie.kiosque.eae.dto;

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

import java.util.List;

public class EaePlanActionDto {

	private int						idEae;
	private List<String>			moyensAutres;
	private List<String>			moyensFinanciers;
	private List<String>			moyensMateriels;
	private List<String>			objectifsIndividuels;
	private List<EaeObjectifProDto>	objectifsProfessionnels;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<String> getMoyensAutres() {
		return moyensAutres;
	}

	public void setMoyensAutres(List<String> moyensAutres) {
		this.moyensAutres = moyensAutres;
	}

	public List<String> getMoyensFinanciers() {
		return moyensFinanciers;
	}

	public void setMoyensFinanciers(List<String> moyensFinanciers) {
		this.moyensFinanciers = moyensFinanciers;
	}

	public List<String> getMoyensMateriels() {
		return moyensMateriels;
	}

	public void setMoyensMateriels(List<String> moyensMateriels) {
		this.moyensMateriels = moyensMateriels;
	}

	public List<String> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<String> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}

	public List<EaeObjectifProDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(List<EaeObjectifProDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}
}
