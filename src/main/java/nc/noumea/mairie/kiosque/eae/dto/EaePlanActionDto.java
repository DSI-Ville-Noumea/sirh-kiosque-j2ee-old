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

	private int idEae;
	private List<EaeItemPlanActionDto> moyensAutres;
	private List<EaeItemPlanActionDto> moyensFinanciers;
	private List<EaeItemPlanActionDto> moyensMateriels;
	private List<EaeItemPlanActionDto> objectifsIndividuels;
	private List<EaeObjectifProDto> objectifsProfessionnels;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<EaeObjectifProDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(List<EaeObjectifProDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}

	public List<EaeItemPlanActionDto> getMoyensAutres() {
		return moyensAutres;
	}

	public void setMoyensAutres(List<EaeItemPlanActionDto> moyensAutres) {
		this.moyensAutres = moyensAutres;
	}

	public List<EaeItemPlanActionDto> getMoyensFinanciers() {
		return moyensFinanciers;
	}

	public void setMoyensFinanciers(List<EaeItemPlanActionDto> moyensFinanciers) {
		this.moyensFinanciers = moyensFinanciers;
	}

	public List<EaeItemPlanActionDto> getMoyensMateriels() {
		return moyensMateriels;
	}

	public void setMoyensMateriels(List<EaeItemPlanActionDto> moyensMateriels) {
		this.moyensMateriels = moyensMateriels;
	}

	public List<EaeItemPlanActionDto> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<EaeItemPlanActionDto> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}
}
