package nc.noumea.mairie.kiosque.abs.dto;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;

public class ApprobateurDto {

	private AgentWithServiceDto approbateur;
	private AgentDto delegataire;

	public AgentDto getDelegataire() {
		return delegataire;
	}

	public void setDelegataire(AgentDto delegataire) {
		this.delegataire = delegataire;
	}

	public AgentWithServiceDto getApprobateur() {
		return approbateur;
	}

	public void setApprobateur(AgentWithServiceDto approbateur) {
		this.approbateur = approbateur;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(null == obj) {
			return false;
		}
		
		return approbateur.getIdAgent().equals(((ApprobateurDto) obj).getApprobateur().getIdAgent());
	}
}
