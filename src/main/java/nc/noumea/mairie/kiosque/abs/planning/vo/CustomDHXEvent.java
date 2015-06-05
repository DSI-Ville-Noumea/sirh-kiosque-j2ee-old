package nc.noumea.mairie.kiosque.abs.planning.vo;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
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

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatEnum;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeGroupeAbsenceEnum;

import com.dhtmlx.planner.DHXEvent;

public class CustomDHXEvent extends DHXEvent {

	// /!\ attention laisser les attributs en public pour le JS pour que ca fonctionne /!\ 
	public String textColor;
	public String color;
	public String user;
	public String textInfoBulle;
	public String typeAbsence;
	public String etatAbsence;
	public String dateDebut;
	public String dateFin;
	public String className;
    
    public CustomDHXEvent(DemandeDto dto) {

		this.setStart_date(dto.getDateDebut());
		this.setEnd_date(dto.getDateFin());
		this.setText("");
		this.textColor = "white";
		this.user = dto.getAgentWithServiceDto().getIdAgent().toString();
		this.textInfoBulle = dto.getAgentWithServiceDto().getNom() + " " + dto.getAgentWithServiceDto().getPrenom();
		this.typeAbsence = dto.getLibelleTypeDemande();
		this.etatAbsence = null != dto.getIdRefEtat() ? RefEtatEnum.getRefEtatEnum(dto.getIdRefEtat()).getLibEtat() : "";
		
		switch(RefTypeGroupeAbsenceEnum.getRefTypeGroupeAbsenceEnum(dto.getGroupeAbsence().getIdRefGroupeAbsence())) {
			case CONGES_ANNUELS:
				this.color = "green";
				break;
			case CONGES_EXCEP:
				this.color = "orange";
				break;
			case AS:
				this.color = "red";
				break;
			case REPOS_COMP:
				this.color = "blue";
				break;
			case RECUP:
				this.color = "aqua";
				break;
			case NOT_EXIST:
				break;
			default:
				break;
		}
		
		switch(RefEtatEnum.getRefEtatEnum(dto.getIdRefEtat())) {
			case PROVISOIRE:
				this.color = "grey";
				break;
			case SAISIE:
			case VISEE_FAVORABLE:
			case VISEE_DEFAVORABLE:
			case EN_ATTENTE:
			case A_VALIDER:
				className = "eventRaye";
				break;
			case APPROUVEE:
			case VALIDEE:
			case PRISE:

				break;
			case REFUSEE:
			case REJETE:
			case ANNULEE:

				break;
		}
    }
    
    public String getTextColor() {
            return textColor;
    }
    public void setTextColor(String textColor) {
            this.textColor = textColor;
    }

    public String getColor() {
            return color;
    }
    public void setColor(String color) {
            this.color = color;
    }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTypeAbsence() {
		return typeAbsence;
	}

	public void setTypeAbsence(String typeAbsence) {
		this.typeAbsence = typeAbsence;
	}

	public String getEtatAbsence() {
		return etatAbsence;
	}

	public void setEtatAbsence(String etatAbsence) {
		this.etatAbsence = etatAbsence;
	}

	public String getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}

	public String getTextInfoBulle() {
		return textInfoBulle;
	}

	public void setTextInfoBulle(String textInfoBulle) {
		this.textInfoBulle = textInfoBulle;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
