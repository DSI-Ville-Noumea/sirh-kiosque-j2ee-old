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

public class EaeObjectifDto {

	private String commentaire;
	private Integer idEaeResultat;
	private String objectif;
	private String resultat;

	public EaeObjectifDto() {
		super();
		this.commentaire = "";
		this.objectif = "";
		this.resultat = "";
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getIdEaeResultat() {
		return idEaeResultat;
	}

	public void setIdEaeResultat(Integer idEaeResultat) {
		this.idEaeResultat = idEaeResultat;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

}
