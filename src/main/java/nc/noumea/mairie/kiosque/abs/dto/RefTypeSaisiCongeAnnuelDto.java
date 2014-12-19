package nc.noumea.mairie.kiosque.abs.dto;

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

public class RefTypeSaisiCongeAnnuelDto {

	private Integer idRefTypeSaisiCongeAnnuel;
	private String codeBaseHoraireAbsence;
	private String description;
	private boolean calendarDateDebut;
	private boolean chkDateDebut;
	private boolean calendarDateFin;
	private boolean chkDateFin;
	private boolean calendarDateReprise;
	private Integer quotaMultiple;
	private boolean decompteSamedi;
	private boolean consecutif;

	public RefTypeSaisiCongeAnnuelDto() {
	}

	public Integer getIdRefTypeSaisiCongeAnnuel() {
		return idRefTypeSaisiCongeAnnuel;
	}

	public void setIdRefTypeSaisiCongeAnnuel(Integer idRefTypeSaisiCongeAnnuel) {
		this.idRefTypeSaisiCongeAnnuel = idRefTypeSaisiCongeAnnuel;
	}

	public String getCodeBaseHoraireAbsence() {
		return codeBaseHoraireAbsence;
	}

	public void setCodeBaseHoraireAbsence(String codeBaseHoraireAbsence) {
		this.codeBaseHoraireAbsence = codeBaseHoraireAbsence;
	}

	public boolean isCalendarDateDebut() {
		return calendarDateDebut;
	}

	public void setCalendarDateDebut(boolean calendarDateDebut) {
		this.calendarDateDebut = calendarDateDebut;
	}

	public boolean isChkDateDebut() {
		return chkDateDebut;
	}

	public void setChkDateDebut(boolean chkDateDebut) {
		this.chkDateDebut = chkDateDebut;
	}

	public boolean isCalendarDateFin() {
		return calendarDateFin;
	}

	public void setCalendarDateFin(boolean calendarDateFin) {
		this.calendarDateFin = calendarDateFin;
	}

	public boolean isChkDateFin() {
		return chkDateFin;
	}

	public void setChkDateFin(boolean chkDateFin) {
		this.chkDateFin = chkDateFin;
	}

	public boolean isCalendarDateReprise() {
		return calendarDateReprise;
	}

	public void setCalendarDateReprise(boolean calendarDateReprise) {
		this.calendarDateReprise = calendarDateReprise;
	}

	public Integer getQuotaMultiple() {
		return quotaMultiple;
	}

	public void setQuotaMultiple(Integer quotaMultiple) {
		this.quotaMultiple = quotaMultiple;
	}

	public boolean isDecompteSamedi() {
		return decompteSamedi;
	}

	public void setDecompteSamedi(boolean decompteSamedi) {
		this.decompteSamedi = decompteSamedi;
	}

	public boolean isConsecutif() {
		return consecutif;
	}

	public void setConsecutif(boolean consecutif) {
		this.consecutif = consecutif;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
