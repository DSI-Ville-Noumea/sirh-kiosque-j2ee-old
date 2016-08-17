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

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

public class EaeListItemDto {

	private Integer			idEae;
	private AgentDto		agentEvalue;
	private List<EaeEvaluateurDto>	eaeEvaluateurs;
	private AgentDto		agentDelegataire;
	private String			etat;
	private boolean			cap;
	private String			avisShd;
	private boolean			docAttache;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date			dateCreation;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date			dateFinalisation;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date			dateControle;
	private boolean			droitInitialiser;
	private boolean			droitAcceder;
	private boolean			droitDemarrer;
	private boolean			droitAffecterDelegataire;
	private boolean			droitImprimerBirt;
	private boolean			droitImprimerGed;
	private String			idDocumentGed;
	private boolean			estDetache;

	private String			directionService;
	private String			sectionService;
	private String			service;

	private AgentDto		agentShd;

	public Integer getIdEae() {
		return idEae;
	}

	public void setIdEae(Integer idEae) {
		this.idEae = idEae;
	}

	public AgentDto getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(AgentDto agentEvalue) {
		this.agentEvalue = agentEvalue;
	}

	public List<EaeEvaluateurDto> getEaeEvaluateurs() {
		return eaeEvaluateurs;
	}

	public void setEaeEvaluateurs(List<EaeEvaluateurDto> eaeEvaluateurs) {
		this.eaeEvaluateurs = eaeEvaluateurs;
	}

	public AgentDto getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(AgentDto agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public String getAvisShd() {
		return avisShd;
	}

	public void setAvisShd(String avisShd) {
		this.avisShd = avisShd;
	}

	public boolean isDocAttache() {
		return docAttache;
	}

	public void setDocAttache(boolean docAttache) {
		this.docAttache = docAttache;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateFinalisation() {
		return dateFinalisation;
	}

	public void setDateFinalisation(Date dateFinalisation) {
		this.dateFinalisation = dateFinalisation;
	}

	public Date getDateControle() {
		return dateControle;
	}

	public void setDateControle(Date dateControle) {
		this.dateControle = dateControle;
	}

	public boolean isDroitInitialiser() {
		return droitInitialiser;
	}

	public void setDroitInitialiser(boolean droitInitialiser) {
		this.droitInitialiser = droitInitialiser;
	}

	public boolean isDroitAcceder() {
		return droitAcceder;
	}

	public void setDroitAcceder(boolean droitAcceder) {
		this.droitAcceder = droitAcceder;
	}

	public boolean isDroitDemarrer() {
		return droitDemarrer;
	}

	public void setDroitDemarrer(boolean droitDemarrer) {
		this.droitDemarrer = droitDemarrer;
	}

	public boolean isDroitAffecterDelegataire() {
		return droitAffecterDelegataire;
	}

	public void setDroitAffecterDelegataire(boolean droitAffecterDelegataire) {
		this.droitAffecterDelegataire = droitAffecterDelegataire;
	}

	public boolean isDroitImprimerBirt() {
		return droitImprimerBirt;
	}

	public void setDroitImprimerBirt(boolean droitImprimerBirt) {
		this.droitImprimerBirt = droitImprimerBirt;
	}

	public boolean isDroitImprimerGed() {
		return droitImprimerGed;
	}

	public void setDroitImprimerGed(boolean droitImprimerGed) {
		this.droitImprimerGed = droitImprimerGed;
	}

	public String getIdDocumentGed() {
		return idDocumentGed;
	}

	public void setIdDocumentGed(String idDocumentGed) {
		this.idDocumentGed = idDocumentGed;
	}

	public boolean isEstDetache() {
		return estDetache;
	}

	public void setEstDetache(boolean estDetache) {
		this.estDetache = estDetache;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getSectionService() {
		return sectionService;
	}

	public void setSectionService(String sectionService) {
		this.sectionService = sectionService;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public AgentDto getAgentShd() {
		return agentShd;
	}

	public void setAgentShd(AgentDto agentShd) {
		this.agentShd = agentShd;
	}
}
