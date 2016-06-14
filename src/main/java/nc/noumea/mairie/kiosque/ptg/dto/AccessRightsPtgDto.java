package nc.noumea.mairie.kiosque.ptg.dto;

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


public class AccessRightsPtgDto {

	private boolean gestionDroitsAcces;
	private boolean fiches;
	private boolean saisie;
	private boolean visualisation;
	private boolean approbation;
	private boolean titreRepas;
	// concerne la Indemnité forfaitaire travail DPM #30544
	private boolean primeDpm;
	private boolean saisiePrimesDpmOperateur;

	public boolean isGestionDroitsAcces() {
		return gestionDroitsAcces;
	}

	public void setGestionDroitsAcces(boolean gestionDroitsAcces) {
		this.gestionDroitsAcces = gestionDroitsAcces;
	}

	public boolean isFiches() {
		return fiches;
	}

	public void setFiches(boolean fiches) {
		this.fiches = fiches;
	}

	public boolean isSaisie() {
		return saisie;
	}

	public void setSaisie(boolean saisie) {
		this.saisie = saisie;
	}

	public boolean isVisualisation() {
		return visualisation;
	}

	public void setVisualisation(boolean visualisation) {
		this.visualisation = visualisation;
	}

	public boolean isApprobation() {
		return approbation;
	}

	public void setApprobation(boolean approbation) {
		this.approbation = approbation;
	}

	public boolean isTitreRepas() {
		return titreRepas;
	}

	public void setTitreRepas(boolean titreRepas) {
		this.titreRepas = titreRepas;
	}

	public boolean isPrimeDpm() {
		return primeDpm;
	}

	public void setPrimeDpm(boolean primeDpm) {
		this.primeDpm = primeDpm;
	}

	public boolean isSaisiePrimesDpmOperateur() {
		return saisiePrimesDpmOperateur;
	}

	public void setSaisiePrimesDpmOperateur(boolean saisiePrimesDpmOperateur) {
		this.saisiePrimesDpmOperateur = saisiePrimesDpmOperateur;
	}
	
}
