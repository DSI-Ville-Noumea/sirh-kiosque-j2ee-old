package nc.noumea.mairie.kiosque.profil.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EnfantDto {

	private Date dateNaissance;
	private String aCharge;
	private String nom;
	private String prenom;
	private String sexe;
	private String lieuNaissance;

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public String getDateNaissanceToString() {
		return new SimpleDateFormat("dd/MM/yyyy").format(dateNaissance);
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getaCharge() {
		return aCharge;
	}

	public void setaCharge(String aCharge) {
		this.aCharge = aCharge;
	}

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

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

}
