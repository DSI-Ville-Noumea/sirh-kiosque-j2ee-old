package nc.noumea.mairie.kiosque.travail.dto;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
