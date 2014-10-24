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


import java.util.ArrayList;
import java.util.List;

public class EaeResultatDto {

	private String commentaireGeneral;
	private int idEae;
	private List<EaeObjectifDto> objectifsIndividuels;
	private List<EaeObjectifDto> objectifsProfessionnels;

	public EaeResultatDto() {
		objectifsIndividuels = new ArrayList<EaeObjectifDto>();
		objectifsProfessionnels = new ArrayList<EaeObjectifDto>();
	}

	public String getCommentaireGeneral() {
		return commentaireGeneral;
	}

	public void setCommentaireGeneral(String commentaireGeneral) {
		this.commentaireGeneral = commentaireGeneral;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<EaeObjectifDto> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<EaeObjectifDto> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}

	public List<EaeObjectifDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(List<EaeObjectifDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}
}
