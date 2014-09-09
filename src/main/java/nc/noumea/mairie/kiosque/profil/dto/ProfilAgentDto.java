package nc.noumea.mairie.kiosque.profil.dto;

import java.text.SimpleDateFormat;
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

	public String getDateNaissanceToString() {
		return new SimpleDateFormat("dd/MM/yyyy").format(dateNaissance);
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
