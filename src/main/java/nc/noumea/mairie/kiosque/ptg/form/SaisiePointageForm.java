package nc.noumea.mairie.kiosque.ptg.form;

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


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.ptg.dto.AbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.HeureSupDto;
import nc.noumea.mairie.kiosque.ptg.dto.PrimeDto;

public class SaisiePointageForm {
	
	List<Date> listDateJour;
	List<String> listVide;
	
	Map<String, List<PrimeDto>> mapAllPrime;
	Map<String, List<AbsenceDto>> mapAllAbsence;
	Map<String, List<HeureSupDto>> mapAllHSup;
	
	public SaisiePointageForm() {
		listDateJour = new ArrayList<Date>();
		listVide = new ArrayList<String>();
		mapAllPrime = new HashMap<String, List<PrimeDto>>();
		mapAllAbsence = new HashMap<String, List<AbsenceDto>>();
		mapAllHSup = new HashMap<String, List<HeureSupDto>>();
	}
	
	public List<Date> getListDateJour() {
		return listDateJour;
	}
	public void setListDateJour(List<Date> listDateJour) {
		this.listDateJour = listDateJour;
	}

	public Map<String, List<PrimeDto>> getMapAllPrime() {
		return mapAllPrime;
	}

	public void setMapAllPrime(Map<String, List<PrimeDto>> mapAllPrime) {
		this.mapAllPrime = mapAllPrime;
	}

	public Map<String, List<AbsenceDto>> getMapAllAbsence() {
		return mapAllAbsence;
	}

	public void setMapAllAbsence(Map<String, List<AbsenceDto>> mapAllAbsence) {
		this.mapAllAbsence = mapAllAbsence;
	}

	public Map<String, List<HeureSupDto>> getMapAllHSup() {
		return mapAllHSup;
	}

	public void setMapAllHSup(Map<String, List<HeureSupDto>> mapAllHSup) {
		this.mapAllHSup = mapAllHSup;
	}

	public List<String> getListVide() {
		return listVide;
	}

	public void setListVide(List<String> listVide) {
		this.listVide = listVide;
	}
	
	public boolean isAfficheAjouterLigneAbsence() {
		return null != this.getMapAllAbsence() 
				&& 1 == this.getMapAllAbsence().size();
	}
	
	public boolean isAfficheAjouterLigneHSup() {
		return null != this.getMapAllHSup()
				&& 1 == this.getMapAllHSup().size();
	}
}
