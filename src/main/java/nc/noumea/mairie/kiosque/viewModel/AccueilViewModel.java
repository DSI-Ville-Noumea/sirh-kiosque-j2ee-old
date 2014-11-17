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
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AccueilViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<AccueilRhDto> listeTexteAccueil;

	@Init
	public void initAccueil() {
		List<AccueilRhDto> listeTexte = sirhWsConsumer.getListeTexteAccueil();
		setListeTexteAccueil(listeTexte);
	}

	@Command
	public void referentRH() {

		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("referentRH.zul", null, null);
		win.doModal();
	}

	public List<AccueilRhDto> getListeTexteAccueil() {
		return listeTexteAccueil;
	}

	public void setListeTexteAccueil(List<AccueilRhDto> listeTexteAccueil) {
		this.listeTexteAccueil = listeTexteAccueil;
	}
}
