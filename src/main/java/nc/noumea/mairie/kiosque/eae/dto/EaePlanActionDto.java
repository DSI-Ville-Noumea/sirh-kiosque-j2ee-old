package nc.noumea.mairie.kiosque.eae.dto;

import java.util.List;

public class EaePlanActionDto {

	private int idEae;
	private String[] moyensAutres;
	private String[] moyensFinanciers;
	private String[] moyensMateriels;
	private String[] objectifsIndividuels;
	private List<EaeObjectifProDto> objectifsProfessionnels;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String[] getMoyensAutres() {
		return moyensAutres;
	}

	public void setMoyensAutres(String[] moyensAutres) {
		this.moyensAutres = moyensAutres;
	}

	public String[] getMoyensFinanciers() {
		return moyensFinanciers;
	}

	public void setMoyensFinanciers(String[] moyensFinanciers) {
		this.moyensFinanciers = moyensFinanciers;
	}

	public String[] getMoyensMateriels() {
		return moyensMateriels;
	}

	public void setMoyensMateriels(String[] moyensMateriels) {
		this.moyensMateriels = moyensMateriels;
	}

	public String[] getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(String[] objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}

	public List<EaeObjectifProDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(List<EaeObjectifProDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}
}
