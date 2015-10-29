package nc.noumea.mairie.kiosque.ptg.dto;

import java.util.Date;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TitreRepasDemandeDto {

	private Integer idTrDemande;
	private AgentWithServiceDto agent;
	private Integer idRefEtat;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateSaisie;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateMonth;
	private Boolean commande;
	private String commentaire;
	private AgentWithServiceDto operateur;

	public TitreRepasDemandeDto() {
		super();
	}

	public Integer getIdTrDemande() {
		return idTrDemande;
	}

	public void setIdTrDemande(Integer idTrDemande) {
		this.idTrDemande = idTrDemande;
	}

	public AgentWithServiceDto getAgent() {
		return agent;
	}

	public void setAgent(AgentWithServiceDto agent) {
		this.agent = agent;
	}

	public Integer getIdRefEtat() {
		return idRefEtat;
	}

	public void setIdRefEtat(Integer idRefEtat) {
		this.idRefEtat = idRefEtat;
	}

	public Date getDateSaisie() {
		return dateSaisie;
	}

	public void setDateSaisie(Date dateSaisie) {
		this.dateSaisie = dateSaisie;
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

	public AgentWithServiceDto getOperateur() {
		return operateur;
	}

	public void setOperateur(AgentWithServiceDto operateur) {
		this.operateur = operateur;
	}

}
