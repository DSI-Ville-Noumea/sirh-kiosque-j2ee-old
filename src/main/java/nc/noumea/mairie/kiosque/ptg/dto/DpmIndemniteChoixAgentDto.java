package nc.noumea.mairie.kiosque.ptg.dto;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.JsonDateDeserializer;
import nc.noumea.mairie.kiosque.dto.JsonDateSerializer;
import nc.noumea.mairie.kiosque.ptg.viewModel.MesChoixPrimeDpmViewModel;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class DpmIndemniteChoixAgentDto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3264579691396835041L;
	
	private Integer idDpmIndemChoixAgent;
	private DpmIndemniteAnneeDto dpmIndemniteAnnee;
	private Integer idAgent;
	private Integer idAgentCreation;
	private AgentWithServiceDto agent;
	private AgentWithServiceDto agentOperateur;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateMaj;
	private boolean isChoixRecuperation;
	private boolean isChoixIndemnite;
	
	@JsonIgnore
	private String radioButtonZK;
	
	public DpmIndemniteChoixAgentDto() {
	}
	
	public Integer getIdDpmIndemChoixAgent() {
		return idDpmIndemChoixAgent;
	}
	public void setIdDpmIndemChoixAgent(Integer idDpmIndemChoixAgent) {
		this.idDpmIndemChoixAgent = idDpmIndemChoixAgent;
	}
	
	public DpmIndemniteAnneeDto getDpmIndemniteAnnee() {
		return dpmIndemniteAnnee;
	}
	public void setDpmIndemniteAnnee(DpmIndemniteAnneeDto dpmIndemniteAnnee) {
		this.dpmIndemniteAnnee = dpmIndemniteAnnee;
	}

	public Integer getIdAgent() {
		return idAgent;
	}
	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}
	
	public Integer getIdAgentCreation() {
		return idAgentCreation;
	}
	public void setIdAgentCreation(Integer idAgentCreation) {
		this.idAgentCreation = idAgentCreation;
	}
	
	public Date getDateMaj() {
		return dateMaj;
	}
	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}
	
	public boolean isChoixRecuperation() {
		return isChoixRecuperation;
	}
	public void setChoixRecuperation(boolean isChoixRecuperation) {
		this.isChoixRecuperation = isChoixRecuperation;
	}
	
	public boolean isChoixIndemnite() {
		return isChoixIndemnite;
	}
	public void setChoixIndemnite(boolean isChoixIndemnite) {
		this.isChoixIndemnite = isChoixIndemnite;
	}

	public AgentWithServiceDto getAgent() {
		return agent;
	}

	public void setAgent(AgentWithServiceDto agent) {
		this.agent = agent;
	}

	public AgentWithServiceDto getAgentOperateur() {
		return agentOperateur;
	}

	public void setAgentOperateur(AgentWithServiceDto agentOperateur) {
		this.agentOperateur = agentOperateur;
	}

	@Transient
	public String getRadioButtonZK() {
		
		if(null == radioButtonZK
				|| "".equals(radioButtonZK.trim())) {
			if(isChoixIndemnite) {
				return MesChoixPrimeDpmViewModel.RADIO_BUTTON_INDEMNITE;
			}
			if(isChoixRecuperation) {
				return MesChoixPrimeDpmViewModel.RADIO_BUTTON_RECUPERATION;
			}
		}
		
		return radioButtonZK;
	}

	public void setRadioButtonZK(String pRadioButtonZK) {
		this.radioButtonZK = pRadioButtonZK;
		
		if(null != pRadioButtonZK
				&& !"".equals(pRadioButtonZK.trim())) {
			setChoixIndemnite(MesChoixPrimeDpmViewModel.RADIO_BUTTON_INDEMNITE.equals(pRadioButtonZK.trim()));
			setChoixRecuperation(MesChoixPrimeDpmViewModel.RADIO_BUTTON_RECUPERATION.equals(pRadioButtonZK.trim()));
		}
	}

	@Override
	public String toString() {
		return "DpmIndemniteChoixAgentDto [idDpmIndemChoixAgent=" + idDpmIndemChoixAgent + ", dpmIndemniteAnnee=" + dpmIndemniteAnnee + ", idAgent="
				+ idAgent + ", idAgentCreation=" + idAgentCreation + ", dateMaj=" + dateMaj + ", isChoixRecuperation=" + isChoixRecuperation
				+ ", isChoixIndemnite=" + isChoixIndemnite + "]";
	}
	
}
