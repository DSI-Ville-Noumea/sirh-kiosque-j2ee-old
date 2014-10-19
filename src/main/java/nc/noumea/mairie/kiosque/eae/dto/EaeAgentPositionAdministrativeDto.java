package nc.noumea.mairie.kiosque.eae.dto;

import java.util.List;

public class EaeAgentPositionAdministrativeDto {

	private String courant;
	private List<PositionAdministrativeDto> liste;

	public String getCourant() {
		return courant;
	}

	public void setCourant(String courant) {
		this.courant = courant;
	}

	public List<PositionAdministrativeDto> getListe() {
		return liste;
	}

	public void setListe(List<PositionAdministrativeDto> liste) {
		this.liste = liste;
	}
}
