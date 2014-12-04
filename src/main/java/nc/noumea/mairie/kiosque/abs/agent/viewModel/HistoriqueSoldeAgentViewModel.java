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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.HistoriqueSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceEnum;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class HistoriqueSoldeAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<HistoriqueSoldeDto> listeHistoriqueSolde;

	private String title;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("agentCourant") Integer idAgent,
			@ExecutionArgParam("codeTypeAbsence") Integer idTypeAbsence) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
		cal.setTime(new Date());
		Integer annee = cal.get(Calendar.YEAR);

		Date dateDeb = new DateTime(annee, 1, 1, 0, 0, 0).toDate();
		Date dateFin = new DateTime(annee, 12, 31, 23, 59, 59).toDate();
		FiltreSoldeDto dto = new FiltreSoldeDto();
		dto.setDateDebut(dateDeb);
		dto.setDateFin(dateFin);

		List<HistoriqueSoldeDto> listeHisto = absWsConsumer.getHistoriqueCompteurAgent(idAgent, idTypeAbsence, dto);
		setListeHistoriqueSolde(listeHisto);

		setTitle("Historique d'alimentation du solde de " + RefTypeAbsenceEnum.getRefTypeAbsenceEnum(idTypeAbsence));
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<HistoriqueSoldeDto> getListeHistoriqueSolde() {
		return listeHistoriqueSolde;
	}

	public void setListeHistoriqueSolde(List<HistoriqueSoldeDto> listeHistoriqueSolde) {
		this.listeHistoriqueSolde = listeHistoriqueSolde;
	}
}
