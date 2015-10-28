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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.TitreRepasDemandeDto;
import nc.noumea.mairie.kiosque.viewModel.AbstractViewModel;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MesTitreRepasViewModel extends AbstractViewModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6673727695582022385L;

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private List<TitreRepasDemandeDto> listeTitreRepas;
	/* POUR LES FILTRES */
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private List<RefEtatPointageDto> listeEtatTitreRepasFiltre;
	private RefEtatPointageDto etatTitreRepasFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initMesTitreRepas() {
		// on recharge les états d'absences pour les filtres
		List<RefEtatPointageDto> filtreEtat = ptgWsConsumer.getEtatTitreRepasKiosque();
		setListeEtatTitreRepasFiltre(filtreEtat);
		setTailleListe("5");
	}

	@Command
	@NotifyChange({ "listeTitreRepas" })
	public void doSearch() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<TitreRepasDemandeDto> list = new ArrayList<TitreRepasDemandeDto>();
		if (getFilter() != null && !"".equals(getFilter()) && getListeTitreRepas() != null) {
			for (TitreRepasDemandeDto item : getListeTitreRepas()) {
				if (sdf.format(item.getDateMonth()).contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setListeTitreRepas(list);
		} else {
			filtrer();
		}
	}

	@Command
	@NotifyChange({ "*" })
	public void filtrer() {

		List<TitreRepasDemandeDto> result = ptgWsConsumer.getListTitreRepas(getCurrentUser().getAgent().getIdAgent(), getDateDebutFiltre(), getDateFinFiltre(), null, getCurrentUser().getAgent().getIdAgent(),
				getEtatTitreRepasFiltre() == null ? null : getEtatTitreRepasFiltre().getIdRefEtat());
		setListeTitreRepas(result);

	}

	@Command
	@NotifyChange({ "dateDebutFiltre", "dateFinFiltre", "etatTitreRepasFiltre" })
	public void viderFiltre() {
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setEtatTitreRepasFiltre(null);
	}

	public String dateSaisieToString(Date dateSaisie) {
		SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
		return sdfJour.format(dateSaisie) + " à " + sdfHeure.format(dateSaisie);
	}

	public String etatToString(Integer idRefEtat) {
		return EtatPointageEnum.getEtatPointageEnum(idRefEtat).getLibEtat();
	}

	public String concatAgentNomatr(AgentDto ag) {
		String nomatr = ag.getIdAgent().toString().substring(3, ag.getIdAgent().toString().length());
		return ag.getNom() + " " + ag.getPrenom() + " (" + nomatr + ")";
	}

	public Date getDateDebutFiltre() {
		return dateDebutFiltre;
	}

	public void setDateDebutFiltre(Date dateDebutFiltre) {
		this.dateDebutFiltre = dateDebutFiltre;
	}

	public Date getDateFinFiltre() {
		return dateFinFiltre;
	}

	public void setDateFinFiltre(Date dateFinFiltre) {
		this.dateFinFiltre = dateFinFiltre;
	}

	public List<RefEtatPointageDto> getListeEtatTitreRepasFiltre() {
		return listeEtatTitreRepasFiltre;
	}

	public void setListeEtatTitreRepasFiltre(List<RefEtatPointageDto> listeEtatTitreRepasFiltre) {
		this.listeEtatTitreRepasFiltre = listeEtatTitreRepasFiltre;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getTailleListe() {
		return tailleListe;
	}

	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
	}

	public List<TitreRepasDemandeDto> getListeTitreRepas() {
		return listeTitreRepas;
	}

	public void setListeTitreRepas(List<TitreRepasDemandeDto> listeTitreRepas) {
		this.listeTitreRepas = listeTitreRepas;
	}

	public RefEtatPointageDto getEtatTitreRepasFiltre() {
		return etatTitreRepasFiltre;
	}

	public void setEtatTitreRepasFiltre(RefEtatPointageDto etatTitreRepasFiltre) {
		this.etatTitreRepasFiltre = etatTitreRepasFiltre;
	}
}
