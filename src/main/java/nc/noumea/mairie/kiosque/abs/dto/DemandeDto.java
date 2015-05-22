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

import java.util.Date;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class DemandeDto {

	private AgentWithServiceDto agentWithServiceDto;

	private Integer idDemande;
	private Integer idTypeDemande;
	private String libelleTypeDemande;
	private RefGroupeAbsenceDto groupeAbsence;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateDemande;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateDebut;
	private boolean isDateDebutAM;
	private boolean isDateDebutPM;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateFin;
	private boolean isDateFinAM;
	private boolean isDateFinPM;
	private Double duree;
	private boolean isSamediOffert;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateReprise;

	private Integer idRefEtat;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateSaisie;
	private String motif;
	private AgentWithServiceDto agentEtat;

	// permet d'afficher ou non les icones correspondants
	private boolean isAffichageBoutonModifier;
	private boolean isAffichageBoutonSupprimer;
	private boolean isAffichageBoutonImprimer;
	private boolean isAffichageBoutonAnnuler;
	private boolean isAffichageVisa;
	private boolean isAffichageApprobation;
	private boolean isAffichageValidation;
	private boolean isAffichageEnAttente;
	private boolean isAffichageBoutonDupliquer;
	// permet de viser ou approuver
	private boolean isModifierVisa;
	private boolean isModifierApprobation;
	private boolean isModifierValidation;
	// valeur du visa et approbation de la demande
	private Boolean isValeurVisa = null;
	private Boolean isValeurApprobation = null;
	private Boolean isValeurValidation = null;
	// depassement de droits
	private boolean isDepassementCompteur;
	private boolean isDepassementMultiple;

	private OrganisationSyndicaleDto organisationSyndicale;

	private String commentaire;

	private RefTypeSaisiDto typeSaisi;
	private RefTypeSaisiCongeAnnuelDto typeSaisiCongeAnnuel;
	
	// #15586 restitution massive
	private boolean affichageBoutonHistorique;

	public DemandeDto() {
	}

	public Integer getIdDemande() {
		return idDemande;
	}

	public void setIdDemande(Integer idDemande) {
		this.idDemande = idDemande;
	}

	public Integer getIdTypeDemande() {
		return idTypeDemande;
	}

	public void setIdTypeDemande(Integer idTypeDemande) {
		this.idTypeDemande = idTypeDemande;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Integer getIdRefEtat() {
		return idRefEtat;
	}

	public void setIdRefEtat(Integer idRefEtat) {
		this.idRefEtat = idRefEtat;
	}

	public Date getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(Date dateDemande) {
		this.dateDemande = dateDemande;
	}

	public boolean isAffichageBoutonModifier() {
		return isAffichageBoutonModifier;
	}

	public void setAffichageBoutonModifier(boolean isAffichageBoutonModifier) {
		this.isAffichageBoutonModifier = isAffichageBoutonModifier;
	}

	public boolean isAffichageBoutonSupprimer() {
		return isAffichageBoutonSupprimer;
	}

	public void setAffichageBoutonSupprimer(boolean isAffichageBoutonSupprimer) {
		this.isAffichageBoutonSupprimer = isAffichageBoutonSupprimer;
	}

	public boolean isAffichageBoutonImprimer() {
		return isAffichageBoutonImprimer;
	}

	public void setAffichageBoutonImprimer(boolean isAffichageBoutonImprimer) {
		this.isAffichageBoutonImprimer = isAffichageBoutonImprimer;
	}

	public boolean isAffichageVisa() {
		return isAffichageVisa;
	}

	public void setAffichageVisa(boolean isAffichageVisa) {
		this.isAffichageVisa = isAffichageVisa;
	}

	public boolean isAffichageApprobation() {
		return isAffichageApprobation;
	}

	public void setAffichageApprobation(boolean isAffichageApprobation) {
		this.isAffichageApprobation = isAffichageApprobation;
	}

	public boolean isAffichageBoutonAnnuler() {
		return isAffichageBoutonAnnuler;
	}

	public void setAffichageBoutonAnnuler(boolean isAffichageBoutonAnnuler) {
		this.isAffichageBoutonAnnuler = isAffichageBoutonAnnuler;
	}

	public Boolean getValeurVisa() {
		return isValeurVisa;
	}

	public void setValeurVisa(Boolean isValeurVisa) {
		this.isValeurVisa = isValeurVisa;
	}

	public Boolean getValeurApprobation() {
		return isValeurApprobation;
	}

	public void setValeurApprobation(Boolean isValeurApprobation) {
		this.isValeurApprobation = isValeurApprobation;
	}

	public boolean isModifierVisa() {
		return isModifierVisa;
	}

	public void setModifierVisa(boolean isModifierVisa) {
		this.isModifierVisa = isModifierVisa;
	}

	public boolean isModifierApprobation() {
		return isModifierApprobation;
	}

	public void setModifierApprobation(boolean isModifierApprobation) {
		this.isModifierApprobation = isModifierApprobation;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public Boolean getIsValeurVisa() {
		return isValeurVisa;
	}

	public void setIsValeurVisa(Boolean isValeurVisa) {
		this.isValeurVisa = isValeurVisa;
	}

	public Boolean getIsValeurApprobation() {
		return isValeurApprobation;
	}

	public void setIsValeurApprobation(Boolean isValeurApprobation) {
		this.isValeurApprobation = isValeurApprobation;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public boolean isDateDebutAM() {
		return isDateDebutAM;
	}

	public void setDateDebutAM(boolean isDateDebutAM) {
		this.isDateDebutAM = isDateDebutAM;
	}

	public boolean isDateDebutPM() {
		return isDateDebutPM;
	}

	public void setDateDebutPM(boolean isDateDebutPM) {
		this.isDateDebutPM = isDateDebutPM;
	}

	public boolean isDateFinAM() {
		return isDateFinAM;
	}

	public void setDateFinAM(boolean isDateFinAM) {
		this.isDateFinAM = isDateFinAM;
	}

	public boolean isDateFinPM() {
		return isDateFinPM;
	}

	public void setDateFinPM(boolean isDateFinPM) {
		this.isDateFinPM = isDateFinPM;
	}

	public Date getDateSaisie() {
		return dateSaisie;
	}

	public void setDateSaisie(Date dateSaisie) {
		this.dateSaisie = dateSaisie;
	}

	public AgentWithServiceDto getAgentWithServiceDto() {
		return agentWithServiceDto;
	}

	public void setAgentWithServiceDto(AgentWithServiceDto agentWithServiceDto) {
		this.agentWithServiceDto = agentWithServiceDto;
	}

	public boolean isDepassementCompteur() {
		return isDepassementCompteur;
	}

	public void setDepassementCompteur(boolean isDepassementCompteur) {
		this.isDepassementCompteur = isDepassementCompteur;
	}

	public boolean isAffichageValidation() {
		return isAffichageValidation;
	}

	public void setAffichageValidation(boolean isAffichageValidation) {
		this.isAffichageValidation = isAffichageValidation;
	}

	public boolean isModifierValidation() {
		return isModifierValidation;
	}

	public void setModifierValidation(boolean isModifierValidation) {
		this.isModifierValidation = isModifierValidation;
	}

	public Boolean getValeurValidation() {
		return isValeurValidation;
	}

	public void setValeurValidation(Boolean isValeurValidation) {
		this.isValeurValidation = isValeurValidation;
	}

	public boolean isAffichageEnAttente() {
		return isAffichageEnAttente;
	}

	public void setAffichageEnAttente(boolean isAffichageEnAttente) {
		this.isAffichageEnAttente = isAffichageEnAttente;
	}

	public boolean isAffichageBoutonDupliquer() {
		return isAffichageBoutonDupliquer;
	}

	public void setAffichageBoutonDupliquer(boolean isAffichageBoutonDupliquer) {
		this.isAffichageBoutonDupliquer = isAffichageBoutonDupliquer;
	}

	public OrganisationSyndicaleDto getOrganisationSyndicale() {
		return organisationSyndicale;
	}

	public void setOrganisationSyndicale(OrganisationSyndicaleDto organisationSyndicale) {
		this.organisationSyndicale = organisationSyndicale;
	}

	public RefGroupeAbsenceDto getGroupeAbsence() {
		return groupeAbsence;
	}

	public void setGroupeAbsence(RefGroupeAbsenceDto groupeAbsence) {
		this.groupeAbsence = groupeAbsence;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public RefTypeSaisiDto getTypeSaisi() {
		return typeSaisi;
	}

	public void setTypeSaisi(RefTypeSaisiDto typeSaisi) {
		this.typeSaisi = typeSaisi;
	}

	public String getLibelleTypeDemande() {
		return libelleTypeDemande;
	}

	public void setLibelleTypeDemande(String libelleTypeDemande) {
		this.libelleTypeDemande = libelleTypeDemande;
	}

	public Double getDuree() {
		return duree;
	}

	public void setDuree(Double duree) {
		this.duree = duree;
	}

	public AgentWithServiceDto getAgentEtat() {
		return agentEtat;
	}

	public void setAgentEtat(AgentWithServiceDto agentEtat) {
		this.agentEtat = agentEtat;
	}

	public RefTypeSaisiCongeAnnuelDto getTypeSaisiCongeAnnuel() {
		return typeSaisiCongeAnnuel;
	}

	public void setTypeSaisiCongeAnnuel(RefTypeSaisiCongeAnnuelDto typeSaisiCongeAnnuel) {
		this.typeSaisiCongeAnnuel = typeSaisiCongeAnnuel;
	}

	public Date getDateReprise() {
		return dateReprise;
	}

	public void setDateReprise(Date dateReprise) {
		this.dateReprise = dateReprise;
	}

	public boolean isDepassementMultiple() {
		return isDepassementMultiple;
	}

	public void setDepassementMultiple(boolean isDepassementMultiple) {
		this.isDepassementMultiple = isDepassementMultiple;
	}

	public boolean isSamediOffert() {
		return isSamediOffert;
	}

	public void setSamediOffert(boolean isSamediOffert) {
		this.isSamediOffert = isSamediOffert;
	}

	public boolean isAffichageBoutonHistorique() {
		return affichageBoutonHistorique;
	}

	public void setAffichageBoutonHistorique(boolean affichageBoutonHistorique) {
		this.affichageBoutonHistorique = affichageBoutonHistorique;
	}

}
