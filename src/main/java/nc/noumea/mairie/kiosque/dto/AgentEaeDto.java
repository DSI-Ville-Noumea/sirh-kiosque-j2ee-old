package nc.noumea.mairie.kiosque.dto;

import java.util.Date;

public class AgentEaeDto {

	private String nom;
	private String nomJeuneFille;
	private String prenom;
	private Integer idAgent;
	private Date dateNaissance;
	private Date dateEntreeCollectivite;
	private Date dateEntreeFonction;
	private Date dateEntreeService;
	private String fonction;

	public AgentEaeDto() {

	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNomJeuneFille() {
		return nomJeuneFille;
	}

	public void setNomJeuneFille(String nomJeuneFille) {
		this.nomJeuneFille = nomJeuneFille;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Date getDateEntreeCollectivite() {
		return dateEntreeCollectivite;
	}

	public void setDateEntreeCollectivite(Date dateEntreeCollectivite) {
		this.dateEntreeCollectivite = dateEntreeCollectivite;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public Date getDateEntreeService() {
		return dateEntreeService;
	}

	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}
}
