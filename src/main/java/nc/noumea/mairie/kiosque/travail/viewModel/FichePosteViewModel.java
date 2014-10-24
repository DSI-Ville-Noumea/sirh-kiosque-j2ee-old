package nc.noumea.mairie.kiosque.travail.viewModel;

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


import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FichePosteViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private FichePosteDto ficheCourant;

	private ProfilAgentDto currentUser;
	
	@Init
	public void initFichePosteAgent() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		
		FichePosteDto result = sirhWsConsumer.getFichePoste(currentUser.getAgent().getIdAgent());
		setFicheCourant(result);
	}

	@Command
	public void imprimeFDP() {
		// on imprime la FDP de l'agent
		byte[] resp = sirhWsConsumer.imprimerFDP(getFicheCourant().getIdFichePoste());
		Filedownload.save(resp, "application/pdf", "fichePoste");
	}

	public FichePosteDto getFicheCourant() {
		return ficheCourant;
	}

	public void setFicheCourant(FichePosteDto ficheCourant) {
		this.ficheCourant = ficheCourant;
	}
}
