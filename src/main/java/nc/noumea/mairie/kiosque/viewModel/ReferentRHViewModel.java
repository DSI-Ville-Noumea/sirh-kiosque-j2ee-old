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

import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ReferentRHViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<ReferentRhDto> listeReferentRh;

	@Init
	public void initReferent() {
		List<ReferentRhDto> listRef = sirhWsConsumer.getListeReferentRH();
		setListeReferentRh(listRef);
	}

	public List<ReferentRhDto> getListeReferentRh() {
		return listeReferentRh;
	}

	public void setListeReferentRh(List<ReferentRhDto> listeReferentRh) {
		this.listeReferentRh = listeReferentRh;
	}
}
