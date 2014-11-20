package nc.noumea.mairie.kiosque.ptg.form;

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
