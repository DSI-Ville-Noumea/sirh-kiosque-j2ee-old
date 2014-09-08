package nc.noumea.mairie.kiosque.profil.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.noumea.mairie.kiosque.dto.AgentGeneriqueDto;

public class EtatCivilDto {

	private AgentGeneriqueDto agent;
	private String sexe;
	private String situationFamiliale;
	private Date dateNaissance;
	private String titre;
	private String lieuNaissance;

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

}
