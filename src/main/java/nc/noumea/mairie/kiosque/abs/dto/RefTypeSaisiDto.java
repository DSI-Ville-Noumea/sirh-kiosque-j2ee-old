package nc.noumea.mairie.kiosque.abs.dto;

import java.io.Serializable;

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



public class RefTypeSaisiDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 247332147340210796L;
	
	private Integer idRefTypeDemande; 
	private boolean calendarDateDebut;
	private boolean calendarHeureDebut;
	private boolean chkDateDebut;
	private boolean calendarDateFin;
	private boolean calendarHeureFin;
	private boolean chkDateFin;
	private boolean duree;
	private boolean pieceJointe;
	private String infosPieceJointe;
	
	private boolean fonctionnaire;
	private boolean contractuel;
	private boolean conventionCollective;
	private boolean compteurCollectif;
	private boolean saisieKiosque;
	private String description;
	private boolean motif;
	private String infosComplementaires;
	private boolean alerte;
	private String messageAlerte;
	private Integer quotaMax;
	private UnitePeriodeQuotaDto unitePeriodeQuotaDto;
	private String uniteDecompte;
	
	// MALADIES
	private boolean prescripteur;
	private boolean dateDeclaration;
	private boolean prolongation;
	private boolean nomEnfant;
	private boolean nombreITT;
	private boolean siegeLesion;
	private boolean atReference;
	private boolean maladiePro;
	private boolean dateAccidentTravail;
	private boolean sansArretTravail;
	
	public RefTypeSaisiDto() {
	}

	public Integer getIdRefTypeDemande() {
		return idRefTypeDemande;
	}

	public void setIdRefTypeDemande(Integer idRefTypeDemande) {
		this.idRefTypeDemande = idRefTypeDemande;
	}

	public boolean isCalendarDateDebut() {
		return calendarDateDebut;
	}

	public void setCalendarDateDebut(boolean calendarDateDebut) {
		this.calendarDateDebut = calendarDateDebut;
	}

	public boolean isCalendarHeureDebut() {
		return calendarHeureDebut;
	}

	public void setCalendarHeureDebut(boolean calendarHeureDebut) {
		this.calendarHeureDebut = calendarHeureDebut;
	}

	public boolean isChkDateDebut() {
		return chkDateDebut;
	}

	public void setChkDateDebut(boolean chkDateDebut) {
		this.chkDateDebut = chkDateDebut;
	}

	public boolean isCalendarDateFin() {
		return calendarDateFin;
	}

	public void setCalendarDateFin(boolean calendarDateFin) {
		this.calendarDateFin = calendarDateFin;
	}

	public boolean isCalendarHeureFin() {
		return calendarHeureFin;
	}

	public void setCalendarHeureFin(boolean calendarHeureFin) {
		this.calendarHeureFin = calendarHeureFin;
	}

	public boolean isChkDateFin() {
		return chkDateFin;
	}

	public void setChkDateFin(boolean chkDateFin) {
		this.chkDateFin = chkDateFin;
	}

	public boolean isDuree() {
		return duree;
	}

	public void setDuree(boolean duree) {
		this.duree = duree;
	}

	public boolean isPieceJointe() {
		return pieceJointe;
	}

	public void setPieceJointe(boolean pieceJointe) {
		this.pieceJointe = pieceJointe;
	}

	public String getInfosPieceJointe() {
		return infosPieceJointe;
	}

	public void setInfosPieceJointe(String infosPieceJointe) {
		this.infosPieceJointe = infosPieceJointe;
	}

	public boolean isFonctionnaire() {
		return fonctionnaire;
	}

	public void setFonctionnaire(boolean fonctionnaire) {
		this.fonctionnaire = fonctionnaire;
	}

	public boolean isContractuel() {
		return contractuel;
	}

	public void setContractuel(boolean contractuel) {
		this.contractuel = contractuel;
	}

	public boolean isConventionCollective() {
		return conventionCollective;
	}

	public void setConventionCollective(boolean conventionCollective) {
		this.conventionCollective = conventionCollective;
	}

	public boolean isSaisieKiosque() {
		return saisieKiosque;
	}

	public void setSaisieKiosque(boolean saisieKiosque) {
		this.saisieKiosque = saisieKiosque;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInfosComplementaires() {
		return infosComplementaires;
	}

	public void setInfosComplementaires(String infosComplementaires) {
		this.infosComplementaires = infosComplementaires;
	}

	public boolean isAlerte() {
		return alerte;
	}

	public void setAlerte(boolean alerte) {
		this.alerte = alerte;
	}

	public String getMessageAlerte() {
		return messageAlerte;
	}

	public void setMessageAlerte(String messageAlerte) {
		this.messageAlerte = messageAlerte;
	}

	public Integer getQuotaMax() {
		return quotaMax;
	}

	public void setQuotaMax(Integer quotaMax) {
		this.quotaMax = quotaMax;
	}

	public UnitePeriodeQuotaDto getUnitePeriodeQuotaDto() {
		return unitePeriodeQuotaDto;
	}

	public void setUnitePeriodeQuotaDto(UnitePeriodeQuotaDto unitePeriodeQuotaDto) {
		this.unitePeriodeQuotaDto = unitePeriodeQuotaDto;
	}

	public String getUniteDecompte() {
		return uniteDecompte;
	}

	public void setUniteDecompte(String uniteDecompte) {
		this.uniteDecompte = uniteDecompte;
	}

	public boolean isCompteurCollectif() {
		return compteurCollectif;
	}

	public void setCompteurCollectif(boolean compteurCollectif) {
		this.compteurCollectif = compteurCollectif;
	}

	public boolean isPrescripteur() {
		return prescripteur;
	}

	public void setPrescripteur(boolean prescripteur) {
		this.prescripteur = prescripteur;
	}

	public boolean isDateDeclaration() {
		return dateDeclaration;
	}

	public void setDateDeclaration(boolean dateDeclaration) {
		this.dateDeclaration = dateDeclaration;
	}

	public boolean isProlongation() {
		return prolongation;
	}

	public void setProlongation(boolean prolongation) {
		this.prolongation = prolongation;
	}

	public boolean isNomEnfant() {
		return nomEnfant;
	}

	public void setNomEnfant(boolean nomEnfant) {
		this.nomEnfant = nomEnfant;
	}

	public boolean isNombreITT() {
		return nombreITT;
	}

	public void setNombreITT(boolean nombreITT) {
		this.nombreITT = nombreITT;
	}

	public boolean isSiegeLesion() {
		return siegeLesion;
	}

	public void setSiegeLesion(boolean siegeLesion) {
		this.siegeLesion = siegeLesion;
	}

	public boolean isAtReference() {
		return atReference;
	}

	public void setAtReference(boolean atReference) {
		this.atReference = atReference;
	}

	public boolean isMaladiePro() {
		return maladiePro;
	}

	public void setMaladiePro(boolean maladiePro) {
		this.maladiePro = maladiePro;
	}

	public boolean isMotif() {
		return motif;
	}

	public void setMotif(boolean motif) {
		this.motif = motif;
	}

	public boolean isDateAccidentTravail() {
		return dateAccidentTravail;
	}

	public void setDateAccidentTravail(boolean dateAccidentTravail) {
		this.dateAccidentTravail = dateAccidentTravail;
	}

	public boolean isSansArretTravail() {
		return sansArretTravail;
	}

	public void setSansArretTravail(boolean sansArretTravail) {
		this.sansArretTravail = sansArretTravail;
	}
	
}
