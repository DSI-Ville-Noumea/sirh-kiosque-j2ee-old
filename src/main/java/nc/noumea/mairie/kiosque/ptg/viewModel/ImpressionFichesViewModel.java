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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.ServiceDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImpressionFichesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private ProfilAgentDto currentUser;

	private List<AgentDto> listeAgents;

	@SuppressWarnings("unused")
	private boolean isChecked;

	private List<String> listeIdAgentsToPrint;
	private Date dateLundi;

	/* POUR LES FILTRES */
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private String dateFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initImpressionFiches() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		setTailleListe("5");
		setDateFiltre("Semaine ... du ... au ...");
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void doSearch() {
		List<AgentDto> list = new ArrayList<AgentDto>();
		if (getFilter() != null && !"".equals(getFilter()) && getListeAgents() != null) {
			for (AgentDto item : getListeAgents()) {
				if (item.getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setListeAgents(list);
		} else {
			afficheListeFiche();
		}
	}

	@Command
	@NotifyChange({ "listeAgents", "dateFiltre" })
	public void afficheListeFiche() {
		if (getDateLundi() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
			c.setTime(getDateLundi());
			String numSemaine = String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			String lundi = sdf.format(c.getTime());
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			String dimanche = sdf.format(c.getTime());

			setDateFiltre("Semaine " + numSemaine + " du " + lundi + " au " + dimanche);
		}
		if (getServiceFiltre() != null && getDateLundi() != null) {
			// on charge les agents pour les filtres
			List<AgentDto> filtreAgent = ptgWsConsumer.getFichesToPrint(currentUser.getAgent().getIdAgent(),
					getServiceFiltre().getCodeService());
			setListeAgents(filtreAgent);
		}
	}

	@Command
	public void checkImpressionFiche(@BindingParam("agent") Integer idAgent, @BindingParam("checkbox") Checkbox checkbox) {
		List<String> list = new ArrayList<>();
		if (getListeIdAgentsToPrint() != null)
			list.addAll(getListeIdAgentsToPrint());
		if (checkbox.isChecked()) {
			list.add(idAgent.toString());
		} else {
			list.remove(idAgent.toString());
		}
		setListeIdAgentsToPrint(list);
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void checkAllImpressionFiche(@BindingParam("checkbox") Checkbox checkbox) {
		if (checkbox.isChecked()) {
			List<String> list = new ArrayList<>();
			for (AgentDto dto : getListeAgents()) {
				list.add(dto.getIdAgent().toString());
			}
			setListeIdAgentsToPrint(list);
		} else {
			setListeIdAgentsToPrint(null);
		}
	}

	@Command
	public void imprimerFiches() {
		if (getListeIdAgentsToPrint() == null || getListeIdAgentsToPrint().size() == 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
			ValidationMessage vm = new ValidationMessage("Veuillez cocher au moins une fiche de pointage à éditer");
			listErreur.add(vm);
			map.put("errors", listErreur);
			map.put("infos", listInfo);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
		} else {
			// on imprime la/les demande(s)
			byte[] resp = ptgWsConsumer.imprimerFiches(currentUser.getAgent().getIdAgent(), getLundi(getDateLundi()),
					getListeIdAgentsToPrint());
			Filedownload.save(resp, "application/pdf", "fichesPointage");
		}
	}

	private Date getLundi(Date dateLundi) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
		c.setTime(dateLundi);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c.getTime();
	}

	public boolean checked(Integer idAgent) {
		if (getListeIdAgentsToPrint() == null)
			return false;
		return getListeIdAgentsToPrint().contains(idAgent.toString());
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String nomatrAgent(Integer idAgent) {
		String idAgentConvert = idAgent.toString();
		return idAgentConvert.substring(3, idAgentConvert.length());
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

	public List<ServiceDto> getListeServicesFiltre() {
		return listeServicesFiltre;
	}

	public void setListeServicesFiltre(List<ServiceDto> listeServicesFiltre) {
		if(null != listeServicesFiltre) {
			Collections.sort(listeServicesFiltre);
		}
		this.listeServicesFiltre = listeServicesFiltre;
	}

	public ServiceDto getServiceFiltre() {
		return serviceFiltre;
	}

	public void setServiceFiltre(ServiceDto serviceFiltre) {
		this.serviceFiltre = serviceFiltre;
	}

	public List<AgentDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentDto> listeAgents) {
		this.listeAgents = listeAgents;
	}

	public List<String> getListeIdAgentsToPrint() {
		return listeIdAgentsToPrint;
	}

	public void setListeIdAgentsToPrint(List<String> listeIdAgentsToPrint) {
		this.listeIdAgentsToPrint = listeIdAgentsToPrint;
	}

	public Date getDateLundi() {
		return dateLundi;
	}

	public void setDateLundi(Date dateLundi) {
		this.dateLundi = dateLundi;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getDateFiltre() {
		return dateFiltre;
	}

	public void setDateFiltre(String dateFiltre) {
		this.dateFiltre = dateFiltre;
	}
}
