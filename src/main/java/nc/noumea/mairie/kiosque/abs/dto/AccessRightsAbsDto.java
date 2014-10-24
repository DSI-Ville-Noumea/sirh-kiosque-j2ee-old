package nc.noumea.mairie.kiosque.abs.dto;

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


public class AccessRightsAbsDto {

	private boolean saisie;
	private boolean modification;
	private boolean suppression;
	private boolean impression;
	private boolean viserVisu;
	private boolean viserModif;
	private boolean approuverVisu;
	private boolean approuverModif;
	private boolean annuler;
	private boolean visuSolde;
	private boolean majSolde;
	private boolean droitAcces;

	public boolean isSaisie() {
		return saisie;
	}

	public void setSaisie(boolean saisie) {
		this.saisie = saisie;
	}

	public boolean isModification() {
		return modification;
	}

	public void setModification(boolean modification) {
		this.modification = modification;
	}

	public boolean isSuppression() {
		return suppression;
	}

	public void setSuppression(boolean suppression) {
		this.suppression = suppression;
	}

	public boolean isImpression() {
		return impression;
	}

	public void setImpression(boolean impression) {
		this.impression = impression;
	}

	public boolean isViserVisu() {
		return viserVisu;
	}

	public void setViserVisu(boolean viserVisu) {
		this.viserVisu = viserVisu;
	}

	public boolean isViserModif() {
		return viserModif;
	}

	public void setViserModif(boolean viserModif) {
		this.viserModif = viserModif;
	}

	public boolean isApprouverVisu() {
		return approuverVisu;
	}

	public void setApprouverVisu(boolean approuverVisu) {
		this.approuverVisu = approuverVisu;
	}

	public boolean isApprouverModif() {
		return approuverModif;
	}

	public void setApprouverModif(boolean approuverModif) {
		this.approuverModif = approuverModif;
	}

	public boolean isAnnuler() {
		return annuler;
	}

	public void setAnnuler(boolean annuler) {
		this.annuler = annuler;
	}

	public boolean isVisuSolde() {
		return visuSolde;
	}

	public void setVisuSolde(boolean visuSolde) {
		this.visuSolde = visuSolde;
	}

	public boolean isMajSolde() {
		return majSolde;
	}

	public void setMajSolde(boolean majSolde) {
		this.majSolde = majSolde;
	}

	public boolean isDroitAcces() {
		return droitAcces;
	}

	public void setDroitAcces(boolean droitAcces) {
		this.droitAcces = droitAcces;
	}
}
