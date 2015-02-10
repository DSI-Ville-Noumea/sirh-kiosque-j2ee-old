package nc.noumea.mairie.kiosque.abs.agent.viewModel;

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
import java.util.HashMap;
import java.util.Map;

import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SoldeAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private SoldeDto soldeCourant;

	private ProfilAgentDto currentUser;

	private String libelleA52;

	@Init
	public void initSoldeAgent() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		FiltreSoldeDto filtreDto = new FiltreSoldeDto();
		filtreDto.setDateDebut(new Date());
		filtreDto.setDateFin(new Date());
		SoldeDto result = absWsConsumer.getAgentSolde(currentUser.getAgent().getIdAgent(), filtreDto);
		setSoldeCourant(result);
		if (getSoldeCourant().getOrganisationA52() != null) {
			setLibelleA52("Décharge de service CTP - " + getSoldeCourant().getOrganisationA52().getSigle());
		} else {
			setLibelleA52("Décharge de service CTP");
		}
	}

	@Command
	public void historiqueSolde(@BindingParam("ref") Integer typeConge) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, Integer> args = new HashMap<String, Integer>();
		args.put("agentCourant", currentUser.getAgent().getIdAgent());
		args.put("codeTypeAbsence", typeConge);
		Window win = (Window) Executions.createComponents("/absences/agent/historiqueSoldeAgent.zul", null, args);
		win.doModal();
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

	public String getTextSamediOffert(boolean solde) {
		if (solde)
			return "pris";
		return "non pris";
	}

	private static String getHeureMinute(int nombreMinute) {
		int heure = nombreMinute / 60;
		int minute = nombreMinute % 60;
		String res = "";
		if (heure > 0)
			res += heure + "h";
		if (minute > 0)
			res += minute + "m";

		return res;
	}

	public SoldeDto getSoldeCourant() {
		return soldeCourant;
	}

	public void setSoldeCourant(SoldeDto soldeCourant) {
		this.soldeCourant = soldeCourant;
	}

	public String getLibelleA52() {
		return libelleA52;
	}

	public void setLibelleA52(String libelleA52) {
		this.libelleA52 = libelleA52;
	}
}
