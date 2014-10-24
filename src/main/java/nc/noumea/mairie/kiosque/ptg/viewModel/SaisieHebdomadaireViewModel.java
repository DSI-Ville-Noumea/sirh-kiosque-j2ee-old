package nc.noumea.mairie.kiosque.ptg.viewModel;

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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.PrimeDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SaisieHebdomadaireViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private FichePointageDto ficheCourante;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private JourPointageDto premierJour;

	@Init
	public void initSaisieFichePointage() throws ParseException {
		
		ProfilAgentDto currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		FichePointageDto result = ptgWsConsumer.getFichePointageSaisie(currentUser.getAgent().getIdAgent(), sdf.parse("08/09/2014"), currentUser.getAgent().getIdAgent());
		setFicheCourante(result);
		setPremierJour(getFicheCourante().getSaisies().get(0));
	}

	@Command
	@NotifyChange({ "ficheCourante", "premierJour" })
	public void checkAccorde(@BindingParam("prime") PrimeDto prime, @BindingParam("checkbox") Checkbox checkbox) {
		prime.setQuantite(checkbox.isChecked() ? 1 : null);
	}

	public FichePointageDto getFicheCourante() {
		return ficheCourante;
	}

	public void setFicheCourante(FichePointageDto ficheCourante) {
		this.ficheCourante = ficheCourante;
	}

	public String getLundi() {
		return sdf.format(getFicheCourante().getDateLundi());
	}

	public String getMardi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 1);
	}

	public String getMercredi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 2);
	}

	public String getJeudi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 3);
	}

	public String getVendredi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 4);
	}

	public String getSamedi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 5);
	}

	public String getDimanche() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 6);
	}

	private String getDatePlusJour(Date dateChoisi, int nbJours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateChoisi);
		cal.add(Calendar.DAY_OF_MONTH, nbJours);
		return sdf.format(cal.getTime());
	}

	public JourPointageDto getPremierJour() {
		return premierJour;
	}

	public void setPremierJour(JourPointageDto premierJour) {
		this.premierJour = premierJour;
	}
}
