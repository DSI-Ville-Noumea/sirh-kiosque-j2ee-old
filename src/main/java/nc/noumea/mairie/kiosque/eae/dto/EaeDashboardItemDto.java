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


public class EaeDashboardItemDto {

	private String nom;
	private String prenom;
	private int nonAffecte;
	private int nonDebute;
	private int cree;
	private int enCours;
	private int finalise;
	private int fige;
	private int nonDefini;
	private int mini;
	private int maxi;
	private int moy;
	private int changClasse;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public int getNonAffecte() {
		return nonAffecte;
	}

	public void setNonAffecte(int nonAffecte) {
		this.nonAffecte = nonAffecte;
	}

	public int getNonDebute() {
		return nonDebute;
	}

	public void setNonDebute(int nonDebute) {
		this.nonDebute = nonDebute;
	}

	public int getCree() {
		return cree;
	}

	public void setCree(int cree) {
		this.cree = cree;
	}

	public int getEnCours() {
		return enCours;
	}

	public void setEnCours(int enCours) {
		this.enCours = enCours;
	}

	public int getFinalise() {
		return finalise;
	}

	public void setFinalise(int finalise) {
		this.finalise = finalise;
	}

	public int getFige() {
		return fige;
	}

	public void setFige(int fige) {
		this.fige = fige;
	}

	public int getNonDefini() {
		return nonDefini;
	}

	public void setNonDefini(int nonDefini) {
		this.nonDefini = nonDefini;
	}

	public int getMini() {
		return mini;
	}

	public void setMini(int mini) {
		this.mini = mini;
	}

	public int getMaxi() {
		return maxi;
	}

	public void setMaxi(int maxi) {
		this.maxi = maxi;
	}

	public int getMoy() {
		return moy;
	}

	public void setMoy(int moy) {
		this.moy = moy;
	}

	public int getChangClasse() {
		return changClasse;
	}

	public void setChangClasse(int changClasse) {
		this.changClasse = changClasse;
	}
}
