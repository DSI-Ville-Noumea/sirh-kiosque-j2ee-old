package nc.noumea.mairie.kiosque.profil.dto;

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

import nc.noumea.mairie.kiosque.dto.AgentGeneriqueDto;

public class ProfilAgentDto {

	private AgentGeneriqueDto agent;
	private String sexe;
	private String situationFamiliale;
	private Date dateNaissance;
	private String titre;
	private String lieuNaissance;
	private AdresseAgentDto adresse;
	private List<ContactAgentDto> listeContacts;
	private List<EnfantDto> listeEnfants;
	private CompteDto compte;
	private CouvertureSocialeDto couvertureSociale;

	public AgentGeneriqueDto getAgent() {
		return agent;
	}

	public void setAgent(AgentGeneriqueDto agent) {
		this.agent = agent;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getSituationFamiliale() {
		return situationFamiliale;
	}

	public void setSituationFamiliale(String situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public AdresseAgentDto getAdresse() {
		return adresse;
	}

	public void setAdresse(AdresseAgentDto adresse) {
		this.adresse = adresse;
	}

	public List<ContactAgentDto> getListeContacts() {
		return listeContacts;
	}

	public void setListeContacts(List<ContactAgentDto> listeContacts) {
		this.listeContacts = listeContacts;
	}

	public List<EnfantDto> getListeEnfants() {
		return listeEnfants;
	}

	public void setListeEnfants(List<EnfantDto> listeEnfants) {
		this.listeEnfants = listeEnfants;
	}

	public CompteDto getCompte() {
		return compte;
	}

	public void setCompte(CompteDto compte) {
		this.compte = compte;
	}

	public CouvertureSocialeDto getCouvertureSociale() {
		return couvertureSociale;
	}

	public void setCouvertureSociale(CouvertureSocialeDto couvertureSociale) {
		this.couvertureSociale = couvertureSociale;
	}

}
