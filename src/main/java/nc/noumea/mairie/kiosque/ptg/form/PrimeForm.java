package nc.noumea.mairie.kiosque.ptg.form;

import java.util.Date;

import nc.noumea.mairie.kiosque.ptg.dto.PrimeDto;

public class PrimeForm extends PrimeDto {
	
	private Date dateJour;

	public Date getDateJour() {
		return dateJour;
	}

	public void setDateJour(Date dateJour) {
		this.dateJour = dateJour;
	}
}
