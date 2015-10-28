package nc.noumea.mairie.kiosque.ptg.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TitreRepasDemandeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1727622068229921626L;

	private Integer idTrDemande;
	private Integer idAgent;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateMonth;
	private Boolean commande;
	private String commentaire;
	private Integer idRefEtat;

	private List<EtatTitreRepasDemandeDto> listEtats;

	public TitreRepasDemandeDto() {
		listEtats = new ArrayList<EtatTitreRepasDemandeDto>();
	}

	public Integer getIdTrDemande() {
		return idTrDemande;
	}

	public void setIdTrDemande(Integer idTrDemande) {
		this.idTrDemande = idTrDemande;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Date getDateMonth() {
		return dateMonth;
	}

	public void setDateMonth(Date dateMonth) {
		this.dateMonth = dateMonth;
	}

	public Boolean getCommande() {
		return commande;
	}

	public void setCommande(Boolean commande) {
		this.commande = commande;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getIdRefEtat() {
		return idRefEtat;
	}

	public void setIdRefEtat(Integer idRefEtat) {
		this.idRefEtat = idRefEtat;
	}

	public List<EtatTitreRepasDemandeDto> getListEtats() {
		return listEtats;
	}

	public void setListEtats(List<EtatTitreRepasDemandeDto> listEtats) {
		this.listEtats = listEtats;
	}

}
