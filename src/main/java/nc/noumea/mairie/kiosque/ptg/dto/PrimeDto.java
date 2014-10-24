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


public class PrimeDto extends PointageDto {

	private String titre;
	private String typeSaisie;
	private Integer quantite;
	private Integer numRubrique;
	private Integer idRefPrime;

	/* POUR L'AFFICHAGE */
	public boolean isPeriodeHeure() {
		return typeSaisie.equals("PERIODE_HEURES");
	}

	public boolean isnbHeures() {
		return typeSaisie.equals("NB_HEURES");
	}

	public boolean isnbIndemnites() {
		return typeSaisie.equals("NB_INDEMNITES");
	}

	public boolean isCaseACocher() {
		return typeSaisie.equals("CASE_A_COCHER");
	}

	public boolean getCheckCoche() {
		return quantite != null;
	}

	public String getLabelCoche() {
		return quantite == null ? "Non" : "Oui";
	}

	public String getEtatToString() {
		if (getIdRefEtat() == null)
			return "Non saisi";
		return EtatPointageEnum.getEtatPointageEnum(getIdRefEtat()).name();
	}

	/* FIN DE POUR L'AFFICHAGE */

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTypeSaisie() {
		return typeSaisie;
	}

	public void setTypeSaisie(String typeSaisie) {
		this.typeSaisie = typeSaisie;
	}

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

	public Integer getNumRubrique() {
		return numRubrique;
	}

	public void setNumRubrique(Integer numRubrique) {
		this.numRubrique = numRubrique;
	}

	public Integer getIdRefPrime() {
		return idRefPrime;
	}

	public void setIdRefPrime(Integer idRefPrime) {
		this.idRefPrime = idRefPrime;
	}

}
