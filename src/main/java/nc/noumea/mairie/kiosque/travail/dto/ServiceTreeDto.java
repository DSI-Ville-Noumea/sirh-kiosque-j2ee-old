package nc.noumea.mairie.kiosque.travail.dto;

import java.util.List;

public class ServiceTreeDto {

	private String service;
	private String serviceLibelle;
	private String sigle;
	private List<ServiceTreeDto> servicesEnfant;

	public ServiceTreeDto() {
		super();
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getServiceLibelle() {
		return serviceLibelle;
	}

	public void setServiceLibelle(String serviceLibelle) {
		this.serviceLibelle = serviceLibelle;
	}

	public String getSigle() {
		return sigle;
	}

	public void setSigle(String sigle) {
		this.sigle = sigle;
	}

	public List<ServiceTreeDto> getServicesEnfant() {
		return servicesEnfant;
	}

	public void setServicesEnfant(List<ServiceTreeDto> servicesEnfant) {
		this.servicesEnfant = servicesEnfant;
	}

}
