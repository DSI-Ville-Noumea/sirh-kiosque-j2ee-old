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

public class EaeEvaluationDto {

	private int idEae;
	private int anneeAvancement;
	private Boolean avisChangementClasse;
	private Boolean avisRevalorisation;
	private boolean cap;
	private String commentaireAvctEvaluateur;
	private String commentaireAvctEvalue;
	private String commentaireEvaluateur;
	private String commentaireEvalue;
	private DureeDto dureeEntretien;
	private EaeListeDto niveau;
	private Double noteAnnee;
	private Double noteAnneeN1;
	private Double noteAnneeN2;
	private Double noteAnneeN3;
	private EaeListeDto propositionAvancement;
	private String statut;
	private String typeAvct;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public int getAnneeAvancement() {
		return anneeAvancement;
	}

	public void setAnneeAvancement(int anneeAvancement) {
		this.anneeAvancement = anneeAvancement;
	}

	public Boolean isAvisChangementClasse() {
		return avisChangementClasse;
	}

	public void setAvisChangementClasse(Boolean avisChangementClasse) {
		this.avisChangementClasse = avisChangementClasse;
	}

	public Boolean isAvisRevalorisation() {
		return avisRevalorisation;
	}

	public void setAvisRevalorisation(Boolean avisRevalorisation) {
		this.avisRevalorisation = avisRevalorisation;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public String getCommentaireAvctEvaluateur() {
		return commentaireAvctEvaluateur;
	}

	public void setCommentaireAvctEvaluateur(String commentaireAvctEvaluateur) {
		this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
	}

	public String getCommentaireAvctEvalue() {
		return commentaireAvctEvalue;
	}

	public void setCommentaireAvctEvalue(String commentaireAvctEvalue) {
		this.commentaireAvctEvalue = commentaireAvctEvalue;
	}

	public String getCommentaireEvaluateur() {
		return commentaireEvaluateur;
	}

	public void setCommentaireEvaluateur(String commentaireEvaluateur) {
		this.commentaireEvaluateur = commentaireEvaluateur;
	}

	public String getCommentaireEvalue() {
		return commentaireEvalue;
	}

	public void setCommentaireEvalue(String commentaireEvalue) {
		this.commentaireEvalue = commentaireEvalue;
	}

	public DureeDto getDureeEntretien() {
		return dureeEntretien;
	}

	public void setDureeEntretien(DureeDto dureeEntretien) {
		this.dureeEntretien = dureeEntretien;
	}

	public EaeListeDto getNiveau() {
		return niveau;
	}

	public void setNiveau(EaeListeDto niveau) {
		this.niveau = niveau;
	}

	public EaeListeDto getPropositionAvancement() {
		return propositionAvancement;
	}

	public void setPropositionAvancement(EaeListeDto propositionAvancement) {
		this.propositionAvancement = propositionAvancement;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getTypeAvct() {
		return typeAvct;
	}

	public void setTypeAvct(String typeAvct) {
		this.typeAvct = typeAvct;
	}

	public Double getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Double noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public Double getNoteAnneeN1() {
		return noteAnneeN1;
	}

	public void setNoteAnneeN1(Double noteAnneeN1) {
		this.noteAnneeN1 = noteAnneeN1;
	}

	public Double getNoteAnneeN2() {
		return noteAnneeN2;
	}

	public void setNoteAnneeN2(Double noteAnneeN2) {
		this.noteAnneeN2 = noteAnneeN2;
	}

	public Double getNoteAnneeN3() {
		return noteAnneeN3;
	}

	public void setNoteAnneeN3(Double noteAnneeN3) {
		this.noteAnneeN3 = noteAnneeN3;
	}
}
