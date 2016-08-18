package nc.noumea.mairie.kiosque.eae.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;

public class EaeFinalizationInformationDto {

	private int				idEae;
	private int				annee;

	private AgentDto		agentEvalue;
	private AgentDto		agentDelegataire;
	private List<AgentDto>	agentsEvaluateurs;
	private List<AgentDto>	agentsShd;
	private Double			noteAnnee;
	private String			commentaire;
	private byte[]			bFile;
	private String			nameFile;

	public EaeFinalizationInformationDto() {
		agentsEvaluateurs = new ArrayList<AgentDto>();
		agentsShd = new ArrayList<AgentDto>();
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public AgentDto getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(AgentDto agentEvalue) {
		this.agentEvalue = agentEvalue;
	}

	public AgentDto getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(AgentDto agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}

	public List<AgentDto> getAgentsEvaluateurs() {
		return agentsEvaluateurs;
	}

	public void setAgentsEvaluateurs(List<AgentDto> agentsEvaluateurs) {
		this.agentsEvaluateurs = agentsEvaluateurs;
	}

	public List<AgentDto> getAgentsShd() {
		return agentsShd;
	}

	public void setAgentsShd(List<AgentDto> agentsShd) {
		this.agentsShd = agentsShd;
	}

	public Double getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Double noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public byte[] getbFile() {
		return bFile;
	}

	public void setbFile(byte[] bFile) {
		this.bFile = bFile;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

}
