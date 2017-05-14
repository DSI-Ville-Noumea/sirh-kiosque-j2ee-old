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
import java.util.Date;

import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class PieceJointeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6418009304707574279L;
	

	private Integer idPieceJointe;
	private byte[] bFile;
	private String typeFile;
	private String titre;
	private String urlFromAlfresco;
	private String nodeRefAlfresco;
	private String commentaire;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateModification;
	
	
	public Integer getIdPieceJointe() {
		return idPieceJointe;
	}
	public void setIdPieceJointe(Integer idPieceJointe) {
		this.idPieceJointe = idPieceJointe;
	}
	public byte[] getbFile() {
		return bFile;
	}
	public void setbFile(byte[] bFile) {
		this.bFile = bFile;
	}
	public String getTypeFile() {
		return typeFile;
	}
	public void setTypeFile(String typeFile) {
		this.typeFile = typeFile;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getUrlFromAlfresco() {
		return urlFromAlfresco;
	}
	public void setUrlFromAlfresco(String urlFromAlfresco) {
		this.urlFromAlfresco = urlFromAlfresco;
	}
	public String getNodeRefAlfresco() {
		return nodeRefAlfresco;
	}
	public void setNodeRefAlfresco(String nodeRefAlfresco) {
		this.nodeRefAlfresco = nodeRefAlfresco;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public Date getDateModification() {
		return dateModification;
	}
	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

}
