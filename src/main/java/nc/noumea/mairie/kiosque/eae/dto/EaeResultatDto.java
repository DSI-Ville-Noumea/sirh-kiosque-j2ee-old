package nc.noumea.mairie.kiosque.eae.dto;

import java.util.ArrayList;
import java.util.List;

public class EaeResultatDto {

	private String commentaireGeneral;
	private int idEae;
	private List<EaeObjectifDto> objectifsIndividuels;
	private List<EaeObjectifDto> objectifsProfessionnels;

	public EaeResultatDto() {
		objectifsIndividuels = new ArrayList<EaeObjectifDto>();
		objectifsProfessionnels = new ArrayList<EaeObjectifDto>();
	}

	public String getCommentaireGeneral() {
		return commentaireGeneral;
	}

	public void setCommentaireGeneral(String commentaireGeneral) {
		this.commentaireGeneral = commentaireGeneral;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<EaeObjectifDto> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<EaeObjectifDto> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}

	public List<EaeObjectifDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(List<EaeObjectifDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}
}
