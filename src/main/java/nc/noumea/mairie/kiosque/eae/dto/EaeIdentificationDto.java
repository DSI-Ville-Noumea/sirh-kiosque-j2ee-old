package nc.noumea.mairie.kiosque.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentEaeDto;

public class EaeIdentificationDto {

	private AgentEaeDto agent;
	private Date dateEntretien;
	private List<String> diplomes;
	private List<String> formations;
	private List<String> parcoursPros;
	private List<AgentEaeDto> evaluateurs;
	private int idEae;
	private EaeAgentPositionAdministrativeDto position;
	private EaeIdentificationSituationDto situation;
	private EaeIdentificationStatutDto statut;

	public EaeIdentificationDto() {
		evaluateurs = new ArrayList<AgentEaeDto>();
		diplomes = new ArrayList<String>();
		parcoursPros = new ArrayList<String>();
		formations = new ArrayList<String>();
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public List<AgentEaeDto> getEvaluateurs() {
		return evaluateurs;
	}

	public void setEvaluateurs(List<AgentEaeDto> evaluateurs) {
		this.evaluateurs = evaluateurs;
	}

	public AgentEaeDto getAgent() {
		return agent;
	}

	public void setAgent(AgentEaeDto agent) {
		this.agent = agent;
	}

	public List<String> getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(List<String> diplomes) {
		this.diplomes = diplomes;
	}

	public List<String> getParcoursPros() {
		return parcoursPros;
	}

	public void setParcoursPros(List<String> parcoursPros) {
		this.parcoursPros = parcoursPros;
	}

	public List<String> getFormations() {
		return formations;
	}

	public void setFormations(List<String> formations) {
		this.formations = formations;
	}

	public EaeIdentificationSituationDto getSituation() {
		return situation;
	}

	public void setSituation(EaeIdentificationSituationDto situation) {
		this.situation = situation;
	}

	public EaeIdentificationStatutDto getStatut() {
		return statut;
	}

	public void setStatut(EaeIdentificationStatutDto statut) {
		this.statut = statut;
	}

	public EaeAgentPositionAdministrativeDto getPosition() {
		return position;
	}

	public void setPosition(EaeAgentPositionAdministrativeDto position) {
		this.position = position;
	}
}
