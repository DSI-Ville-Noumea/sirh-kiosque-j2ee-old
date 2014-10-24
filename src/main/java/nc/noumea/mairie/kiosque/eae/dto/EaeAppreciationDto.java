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


public class EaeAppreciationDto {

	private boolean estEncadrant;
	private int idEae;
	private String[] managerialEvaluateur;
	private String[] managerialEvalue;
	private String[] resultatsEvaluateur;
	private String[] resultatsEvalue;
	private String[] savoirEtreEvaluateur;
	private String[] savoirEtreEvalue;
	private String[] techniqueEvaluateur;
	private String[] techniqueEvalue;

	public boolean isEstEncadrant() {
		return estEncadrant;
	}

	public void setEstEncadrant(boolean estEncadrant) {
		this.estEncadrant = estEncadrant;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String[] getManagerialEvaluateur() {
		return managerialEvaluateur;
	}

	public void setManagerialEvaluateur(String[] managerialEvaluateur) {
		this.managerialEvaluateur = managerialEvaluateur;
	}

	public String[] getManagerialEvalue() {
		return managerialEvalue;
	}

	public void setManagerialEvalue(String[] managerialEvalue) {
		this.managerialEvalue = managerialEvalue;
	}

	public String[] getResultatsEvaluateur() {
		return resultatsEvaluateur;
	}

	public void setResultatsEvaluateur(String[] resultatsEvaluateur) {
		this.resultatsEvaluateur = resultatsEvaluateur;
	}

	public String[] getResultatsEvalue() {
		return resultatsEvalue;
	}

	public void setResultatsEvalue(String[] resultatsEvalue) {
		this.resultatsEvalue = resultatsEvalue;
	}

	public String[] getSavoirEtreEvaluateur() {
		return savoirEtreEvaluateur;
	}

	public void setSavoirEtreEvaluateur(String[] savoirEtreEvaluateur) {
		this.savoirEtreEvaluateur = savoirEtreEvaluateur;
	}

	public String[] getSavoirEtreEvalue() {
		return savoirEtreEvalue;
	}

	public void setSavoirEtreEvalue(String[] savoirEtreEvalue) {
		this.savoirEtreEvalue = savoirEtreEvalue;
	}

	public String[] getTechniqueEvaluateur() {
		return techniqueEvaluateur;
	}

	public void setTechniqueEvaluateur(String[] techniqueEvaluateur) {
		this.techniqueEvaluateur = techniqueEvaluateur;
	}

	public String[] getTechniqueEvalue() {
		return techniqueEvalue;
	}

	public void setTechniqueEvalue(String[] techniqueEvalue) {
		this.techniqueEvalue = techniqueEvalue;
	}
}
