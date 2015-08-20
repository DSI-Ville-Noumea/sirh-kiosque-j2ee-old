package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

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


import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ActeursDto;
import nc.noumea.mairie.kiosque.abs.dto.AgentDto;
import nc.noumea.mairie.kiosque.abs.dto.ApprobateurDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesSoldeAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private SoldeDto soldeCourant;

	private String title;
	
	private ActeursDto acteursDto;

	private String libelleA52;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("demandeCourant") DemandeDto demande) {
		AgentWithServiceDto agent = demande.getAgentWithServiceDto();
		setTitle("Solde de " + agent.getNom() + " " + agent.getPrenom());
		// on recupère le solde de l'agent
		FiltreSoldeDto filtreDto = new FiltreSoldeDto();
		filtreDto.setDateDebut(new Date());
		filtreDto.setDateFin(new Date());
		filtreDto.setDateDemande(demande.getDateDebut());
		SoldeDto result = absWsConsumer.getAgentSolde(agent.getIdAgent(), filtreDto);
		setSoldeCourant(result);
		// #14844 liste des acteurs
		setActeursDto(absWsConsumer.getListeActeurs(agent.getIdAgent()));

		if (getSoldeCourant().getOrganisationA52() != null) {
			setLibelleA52("Décharge de service CTP - " + getSoldeCourant().getOrganisationA52().getSigle());
		} else {
			setLibelleA52("Décharge de service CTP");
		}
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public String soldeJour(Double solde) {
		if (solde == 0)
			return "aucun";
		return solde + " j";
	}

	public String soldeHeure(Double solde) {
		if (solde == 0)
			return "aucun";
		return getHeureMinute(solde.intValue());
	}

	private static String getHeureMinute(int nombreMinute) {
		int heure = nombreMinute / 60;
		int minute = nombreMinute % 60;
		String res = "";
		if (heure != 0)
			res += heure + "h";
		if (minute > 0)
			res += minute + "m";

		return res;
	}
	
	public String formatNomAgent(List<AgentDto> listAgents) {
		
		String result = "";
		if(null != listAgents
				&& !listAgents.isEmpty()) {
			for(AgentDto agent : listAgents) {
				result += agent.getNom() + " " + agent.getPrenom() + " \n";
			}
		}else{
			result = "Aucun";
		}
		return result;
	}
	
	public String formatNomApprobateur(List<ApprobateurDto> listAgents) {
		
		String result = "";
		if(null != listAgents
				&& !listAgents.isEmpty()) {
			for(ApprobateurDto agent : listAgents) {
				result += agent.getApprobateur().getNom() + " " + agent.getApprobateur().getPrenom();
				if(null != agent.getDelegataire()
						&& null != agent.getDelegataire().getNom()
						&& !"".equals(agent.getDelegataire().getNom().trim())) {
					result += " (Délégataire : " + agent.getDelegataire().getNom() + " " + agent.getDelegataire().getPrenom() + ")";
				}
				result += "\n";
			}
		}else{
			result = "Aucun";
		}
		return result;
	}

	public SoldeDto getSoldeCourant() {
		return soldeCourant;
	}

	public void setSoldeCourant(SoldeDto soldeCourant) {
		this.soldeCourant = soldeCourant;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ActeursDto getActeursDto() {
		return acteursDto;
	}

	public void setActeursDto(ActeursDto acteursDto) {
		this.acteursDto = acteursDto;
	}

	public String getLibelleA52() {
		return libelleA52;
	}

	public void setLibelleA52(String libelleA52) {
		this.libelleA52 = libelleA52;
	}
	
}
