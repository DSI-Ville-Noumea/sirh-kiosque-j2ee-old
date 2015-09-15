package nc.noumea.mairie.kiosque.ptg.droits.viewModel;

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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.kiosque.viewModel.AbstractViewModel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DroitsAccesViewModel extends AbstractViewModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AgentDto> listeAgents;

	private List<AgentDto> listeDelegataire;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private boolean afficheAffecterAgent;
	private Tab tabCourant;
	@Init
	public void initDroitsAccesPointage() {

		if (!getDroitsPointage().isGestionDroitsAcces()) {
			Executions.getCurrent().sendRedirect("401.jsp");
		}

		setListeAgents(ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent()));

		setTailleListe("10");
	}

	@Command
	public void ajouterAgent() {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants", ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent()));
			Window win = (Window) Executions.createComponents("/pointages/droits/ajoutAgentApprobateur.zul", null, map);
			win.doModal();
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("operateursExistants", ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent())
					.getSaisisseurs());
			map.put("delegataireExistants", ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent())
					.getDelegataire());
			Window win = (Window) Executions.createComponents("/pointages/droits/ajoutOperateurApprobateur.zul", null,
					map);
			win.doModal();
		}
	}

	@GlobalCommand
	@NotifyChange({ "listeAgents", "listeDelegataire" })
	public void refreshListeAgent() {
		setListeAgents(null);
		setListeDelegataire(null);

		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// on recupere les agents de l'approbateur
			List<AgentDto> result = ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent());
			setListeAgents(result);
			setAfficheAffecterAgent(false);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les operateurs de l'agent
			DelegatorAndOperatorsDto result = ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent());
			setListeAgents(result.getSaisisseurs());
			List<AgentDto> delegataire = null;
			if (result.getDelegataire() != null) {
				delegataire = new ArrayList<>();
				delegataire.add(result.getDelegataire());
			}
			setListeDelegataire(delegataire);
			setAfficheAffecterAgent(true);
		}
	}

	@Command
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
	}

	@Command
	@NotifyChange({ "listeAgents", "listeDelegataire" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		setListeAgents(null);
		setListeDelegataire(null);
		// on sauvegarde l'onglet
		setTabCourant(tab);

		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// on recupere les agents de l'approbateur
			List<AgentDto> result = ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent());
			setListeAgents(result);
			setAfficheAffecterAgent(false);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les op?rateurs de l'agent
			DelegatorAndOperatorsDto result = ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent());
			setListeAgents(result.getSaisisseurs());
			List<AgentDto> delegataire = null;
			if (result.getDelegataire() != null) {
				delegataire = new ArrayList<>();
				delegataire.add(result.getDelegataire());
			}
			setListeDelegataire(delegataire);
			setAfficheAffecterAgent(true);
		}
	}

	@Command
	public void affecterAgent(@BindingParam("ref") AgentDto agent) {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants",
					ptgWsConsumer.getAgentsSaisisOperateur(getCurrentUser().getAgent().getIdAgent(), agent.getIdAgent()));
			map.put("operateur", agent);
			AgentDto approbateur = new AgentDto();
			approbateur.setIdAgent(getCurrentUser().getAgent().getIdAgent());
			map.put("approbateur", approbateur);
			Window win = (Window) Executions.createComponents("/pointages/droits/ajoutAgentOperateur.zul", null, map);
			win.doModal();
		}
	}

	@Command
	public void ajouterDelegataire() {
		if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("operateursExistants", ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent())
					.getSaisisseurs());
			map.put("delegataireExistants", ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent())
					.getDelegataire());
			Window win = (Window) Executions.createComponents("/pointages/droits/ajoutDelegataireApprobateur.zul",
					null, map);
			win.doModal();
		}
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void supprimerAgent(@BindingParam("ref") AgentDto agentASupprimer) {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			supprimerAgentsApprobateurs(agentASupprimer);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			supprimerOperateursApprobateurs(agentASupprimer);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void supprimerTousLesAgents() {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// on ouvre une popup de confirmation
			Messagebox.show("Voulez-vous supprimer tous les agents à approuver?", "Confirmation", Messagebox.CANCEL
					| Messagebox.OK, "", new EventListener() {
				@Override
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onOK")) {
						supprimerTousLesAgentsApprobateurs();
						BindUtils.postNotifyChange(null, null, DroitsAccesViewModel.this, "listeAgents");
					}
				}
			});
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on ouvre une popup de confirmation
			Messagebox.show("Voulez-vous supprimer tous les opérateurs?", "Confirmation", Messagebox.CANCEL
					| Messagebox.OK, "", new EventListener() {
				@Override
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onOK")) {
						supprimerTousLesOperateursApprobateurs();
						BindUtils.postNotifyChange(null, null, DroitsAccesViewModel.this, "listeAgents");
					}
				}
			});
		}
	}

	private void supprimerDelegataireApprobateurs(AgentDto agentDelegataireASupprimer) {
		// on recupere tous les delegataire de l'approbateurs et on supprime
		// l'entree
		if (getListeDelegataire().contains(agentDelegataireASupprimer)) {
			getListeDelegataire().remove(agentDelegataireASupprimer);
		}

		DelegatorAndOperatorsDto dto = new DelegatorAndOperatorsDto();
		dto.setSaisisseurs(getListeAgents());
		dto.setDelegataire(getListeDelegataire().size() == 0 ? null : getListeDelegataire().get(0));
		ReturnMessageDto result = ptgWsConsumer.saveDelegateAndOperator(getCurrentUser().getAgent().getIdAgent(), dto);

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Le délégataire a été enregistré correctement.");
		for (String error : result.getErrors()) {
			ValidationMessage vm = new ValidationMessage(error);
			listErreur.add(vm);
		}
		for (String info : result.getInfos()) {
			ValidationMessage vm = new ValidationMessage(info);
			listInfo.add(vm);
		}
		map.put("errors", listErreur);
		map.put("infos", listInfo);
		Executions.createComponents("/messages/returnMessage.zul", null, map);
		if (listErreur.size() == 0) {
			refreshListeAgent();
		}
	}

	private void supprimerTousLesOperateursApprobateurs() {

		DelegatorAndOperatorsDto dto = new DelegatorAndOperatorsDto();
		dto.setSaisisseurs(new ArrayList<AgentDto>());
		dto.setDelegataire(getListeDelegataire() == null || getListeDelegataire().size() == 0 ? null
				: getListeDelegataire().get(0));
		ReturnMessageDto result = ptgWsConsumer.saveDelegateAndOperator(getCurrentUser().getAgent().getIdAgent(), dto);

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Les opérateurs ont été enregistrés correctement.");
		for (String error : result.getErrors()) {
			ValidationMessage vm = new ValidationMessage(error);
			listErreur.add(vm);
		}
		for (String info : result.getInfos()) {
			ValidationMessage vm = new ValidationMessage(info);
			listInfo.add(vm);
		}
		map.put("errors", listErreur);
		map.put("infos", listInfo);
		Executions.createComponents("/messages/returnMessage.zul", null, map);
		if (listErreur.size() == 0) {
			refreshListeAgent();
		}
	}

	private void supprimerOperateursApprobateurs(AgentDto agentASupprimer) {
		// on recupere tous les operateurs de l'approbateurs et on supprime
		// l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		DelegatorAndOperatorsDto dto = new DelegatorAndOperatorsDto();
		dto.setSaisisseurs(getListeAgents());
		dto.setDelegataire(getListeDelegataire() == null || getListeDelegataire().size() == 0 ? null
				: getListeDelegataire().get(0));
		ReturnMessageDto result = ptgWsConsumer.saveDelegateAndOperator(getCurrentUser().getAgent().getIdAgent(), dto);

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Les opérateurs ont été enregistrés correctement.");
		for (String error : result.getErrors()) {
			ValidationMessage vm = new ValidationMessage(error);
			listErreur.add(vm);
		}
		for (String info : result.getInfos()) {
			ValidationMessage vm = new ValidationMessage(info);
			listInfo.add(vm);
		}
		map.put("errors", listErreur);
		map.put("infos", listInfo);
		Executions.createComponents("/messages/returnMessage.zul", null, map);
		if (listErreur.size() == 0) {
			refreshListeAgent();
		}
	}

	private void supprimerTousLesAgentsApprobateurs() {
		ReturnMessageDto result = ptgWsConsumer.saveApprovedAgents(getCurrentUser().getAgent().getIdAgent(),
				new ArrayList<AgentDto>());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Les agents à approuver ont été enregistrés correctement.");
		for (String error : result.getErrors()) {
			ValidationMessage vm = new ValidationMessage(error);
			listErreur.add(vm);
		}
		for (String info : result.getInfos()) {
			ValidationMessage vm = new ValidationMessage(info);
			listInfo.add(vm);
		}
		map.put("errors", listErreur);
		map.put("infos", listInfo);
		Executions.createComponents("/messages/returnMessage.zul", null, map);

		refreshListeAgent();
	}

	private void supprimerAgentsApprobateurs(AgentDto agentASupprimer) {
		// on recupere tous les agents de l'approbateurs et on supprime l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		ReturnMessageDto result = ptgWsConsumer.saveApprovedAgents(getCurrentUser().getAgent().getIdAgent(),
				getListeAgents());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Les agents à approuver ont été enregistrés correctement.");
		for (String error : result.getErrors()) {
			ValidationMessage vm = new ValidationMessage(error);
			listErreur.add(vm);
		}
		for (String info : result.getInfos()) {
			ValidationMessage vm = new ValidationMessage(info);
			listInfo.add(vm);
		}
		map.put("errors", listErreur);
		map.put("infos", listInfo);
		Executions.createComponents("/messages/returnMessage.zul", null, map);
		if (listErreur.size() == 0) {
			refreshListeAgent();
		}
	}

	@Command
	@NotifyChange({ "listeDelegataire" })
	public void supprimerDelegataire(@BindingParam("ref") AgentDto agentDelegataireASupprimer) {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("OPERATEUR")) {
			supprimerDelegataireApprobateurs(agentDelegataireASupprimer);
		}
	}

	@Command
	public void exportPDF(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<AgentDto> result = new ArrayList<AgentDto>();
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// on recupere les agents de l'approbateur
			result = ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent());
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les opérateurs de l'agent
			result = ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent()).getSaisisseurs();
		}

		// #14688 : ABS+PTG-KIOSQUE : DROITS D'ACCES : revoir l'export excel
		// on efface les données de lalistbox et on remplace par toutes les
		// données
		listbox.getItems().clear();
		if (result.size() == 0) {
			Listitem item = new Listitem();
			Listcell cellImage = new Listcell();
			Listcell cellNom = new Listcell();
			cellNom.setLabel("");
			Listcell cellPrenom = new Listcell();
			cellPrenom.setLabel("");
			item.appendChild(cellNom);
			item.appendChild(cellPrenom);
			item.appendChild(cellImage);
			item.setValue(new AgentDto());
			listbox.getItems().add(item);
		} else {
			Collections.sort(result);
			for (AgentDto a : result) {
				Listitem item = new Listitem();
				Listcell cellImage = new Listcell();
				Listcell cellNom = new Listcell();
				cellNom.setLabel(a.getNom());
				Listcell cellPrenom = new Listcell();
				cellPrenom.setLabel(a.getPrenom());
				item.appendChild(cellNom);
				item.appendChild(cellPrenom);
				item.appendChild(cellImage);
				item.setValue(a);
				listbox.getItems().add(item);
			}
		}

		PdfExporter exporter = new PdfExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("droitsAbsence.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();

		if (result.size() == 0) {
			listbox.getItems().clear();
		}
	}

	@Command
	public void exportExcel(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<AgentDto> result = new ArrayList<AgentDto>();
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// on recupere les agents de l'approbateur
			result = ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent());
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les opérateurs de l'agent
			result = ptgWsConsumer.getDelegateAndOperator(getCurrentUser().getAgent().getIdAgent()).getSaisisseurs();
		}
		// #14688 : ABS+PTG-KIOSQUE : DROITS D'ACCES : revoir l'export excel
		// on efface les données de lalistbox et on remplace par toutes les
		// données
		listbox.getItems().clear();
		Collections.sort(result);
		for (AgentDto a : result) {
			Listitem item = new Listitem();
			Listcell cellImage = new Listcell();
			Listcell cellNom = new Listcell();
			cellNom.setLabel(a.getNom());
			Listcell cellPrenom = new Listcell();
			cellPrenom.setLabel(a.getPrenom());
			item.appendChild(cellNom);
			item.appendChild(cellPrenom);
			item.appendChild(cellImage);
			item.setValue(a);
			listbox.getItems().add(item);
		}

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("droitsAbsence.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	public List<AgentDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentDto> listeAgents) {
		if (null != listeAgents) {
			Collections.sort(listeAgents);
		}
		this.listeAgents = listeAgents;
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

	public boolean isAfficheAffecterAgent() {
		return afficheAffecterAgent;
	}

	public void setAfficheAffecterAgent(boolean afficheAffecterAgent) {
		this.afficheAffecterAgent = afficheAffecterAgent;
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
	}

	public List<AgentDto> getListeDelegataire() {
		return listeDelegataire;
	}

	public void setListeDelegataire(List<AgentDto> listeDelegataire) {
		this.listeDelegataire = listeDelegataire;
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
			setListeAgents(ptgWsConsumer.getApprovedAgents(getCurrentUser().getAgent().getIdAgent()));
		}
	}

}
