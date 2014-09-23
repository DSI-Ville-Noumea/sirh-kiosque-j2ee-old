package nc.noumea.mairie.kiosque.abs.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;

public class ViseursDto {

	private List<AgentDto> viseurs;

	public ViseursDto() {
		viseurs = new ArrayList<AgentDto>();
	}

	public List<AgentDto> getViseurs() {
		return viseurs;
	}

	public void setViseurs(List<AgentDto> viseurs) {
		this.viseurs = viseurs;
	}
}
