package nc.noumea.mairie.kiosque.ptg.dto;

import java.util.Date;
import java.util.List;

public class JourPointageDto {

	private Date date;
	private List<PrimeDto> primes;
	private List<HeureSupDto> heuresSup;
	private List<AbsenceDto> absences;

	public List<PrimeDto> getPrimes() {
		return primes;
	}

	public void setPrimes(List<PrimeDto> primes) {
		this.primes = primes;
	}

	public List<HeureSupDto> getHeuresSup() {
		return heuresSup;
	}

	public void setHeuresSup(List<HeureSupDto> heuresSup) {
		this.heuresSup = heuresSup;
	}

	public List<AbsenceDto> getAbsences() {
		return absences;
	}

	public void setAbsences(List<AbsenceDto> absences) {
		this.absences = absences;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
