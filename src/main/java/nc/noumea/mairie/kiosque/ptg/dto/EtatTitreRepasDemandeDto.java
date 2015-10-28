package nc.noumea.mairie.kiosque.ptg.dto;

import java.io.Serializable;
import java.util.Date;

public class EtatTitreRepasDemandeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9026012609607050164L;

	private Integer idTrEtatDemande;
	private Date dateMaj;
	private Integer etat;
	Integer idAgent;
	Boolean commande;

	public EtatTitreRepasDemandeDto() {
	}

	public Integer getIdTrEtatDemande() {
		return idTrEtatDemande;
	}

	public void setIdTrEtatDemande(Integer idTrEtatDemande) {
		this.idTrEtatDemande = idTrEtatDemande;
	}

	public Date getDateMaj() {
		return dateMaj;
	}

	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}

	public Integer getEtat() {
		return etat;
	}

	public void setEtat(Integer etat) {
		this.etat = etat;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Boolean getCommande() {
		return commande;
	}

	public void setCommande(Boolean commande) {
		this.commande = commande;
	}

}
