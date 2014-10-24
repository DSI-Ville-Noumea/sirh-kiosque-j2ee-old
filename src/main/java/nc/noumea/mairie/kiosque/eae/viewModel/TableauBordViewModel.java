package nc.noumea.mairie.kiosque.eae.viewModel;

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

import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TableauBordViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private List<EaeDashboardItemDto> tableauBord;

	@Init
	public void initTableauBord() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupère les info du tableau de bord
		List<EaeDashboardItemDto> tableau = eaeWsConsumer.getTableauBord(currentUser.getAgent().getIdAgent());
		setTableauBord(tableau);
	}

	public String totalNonAffecte() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonAffecte();
		}
		return String.valueOf(total);
	}

	public String totalNonDebute() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonDebute();
		}
		return String.valueOf(total);
	}

	public String totalCree() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getCree();
		}
		return String.valueOf(total);
	}

	public String totalEnCours() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getEnCours();
		}
		return String.valueOf(total);
	}

	public String totalFinalise() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getFinalise();
		}
		return String.valueOf(total);
	}

	public String totalFige() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getFige();
		}
		return String.valueOf(total);
	}

	public String totalEae() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonAffecte() + dto.getNonDebute() + dto.getCree() + dto.getEnCours() + dto.getFinalise()
					+ dto.getFige();
		}
		return String.valueOf(total);
	}

	public String totalNonDefini() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getNonDefini();
		}
		return String.valueOf(total);
	}

	public String totalMini() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getMini();
		}
		return String.valueOf(total);
	}

	public String totalMoy() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getMoy();
		}
		return String.valueOf(total);
	}

	public String totalMaxi() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getMaxi();
		}
		return String.valueOf(total);
	}

	public String totalChgtClasse() {
		int total = 0;
		for (EaeDashboardItemDto dto : getTableauBord()) {
			total += dto.getChangClasse();
		}
		return String.valueOf(total);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String totalEae(EaeDashboardItemDto dto) {
		int total = dto.getNonAffecte() + dto.getNonDebute() + dto.getCree() + dto.getEnCours() + dto.getFinalise()
				+ dto.getFige();
		return String.valueOf(total);
	}

	public List<EaeDashboardItemDto> getTableauBord() {
		return tableauBord;
	}

	public void setTableauBord(List<EaeDashboardItemDto> tableauBord) {
		this.tableauBord = tableauBord;
	}
}
