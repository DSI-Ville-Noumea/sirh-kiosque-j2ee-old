package nc.noumea.mairie.kiosque.viewModel;

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

import java.util.List;

import nc.noumea.mairie.kiosque.dto.AccueilRhDto;
import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AccueilViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private ProfilAgentDto currentUser;

	private List<AccueilRhDto> listeTexteAccueil;

	private ReferentRhDto refrentRh;

	@Init
	public void initAccueil() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// message d'accueil
		List<AccueilRhDto> listeTexte = sirhWsConsumer.getListeTexteAccueil();
		setListeTexteAccueil(listeTexte);
		// refrent Rh de l'agent
		ReferentRhDto referent = sirhWsConsumer.getReferentRH(currentUser.getAgent().getIdAgent());
		setRefrentRh(referent);

	}

	public String getPrenomAgent(String prenom) {
		if (!prenom.equals("")) {
			String premierLettre = prenom.substring(0, 1).toUpperCase();
			String reste = prenom.substring(1, prenom.length()).toLowerCase();
			return premierLettre + reste;
		}
		return "";
	}

	public List<AccueilRhDto> getListeTexteAccueil() {
		return listeTexteAccueil;
	}

	public void setListeTexteAccueil(List<AccueilRhDto> listeTexteAccueil) {
		this.listeTexteAccueil = listeTexteAccueil;
	}

	public ReferentRhDto getRefrentRh() {
		return refrentRh;
	}

	public void setRefrentRh(ReferentRhDto refrentRh) {
		this.refrentRh = refrentRh;
	}
}
