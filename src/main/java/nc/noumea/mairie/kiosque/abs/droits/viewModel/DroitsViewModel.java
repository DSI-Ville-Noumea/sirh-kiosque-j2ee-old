package nc.noumea.mairie.kiosque.abs.droits.viewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.InputterDto;
import nc.noumea.mairie.kiosque.abs.dto.ViseursDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DroitsViewModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<AgentDto> listeAgents;

	private List<AgentDto> listeDelegataire;

	private Tab tabCourant;

	private boolean afficheAffecterAgent;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initDroits() {
		// on recupere les agents de l'approbateur
		List<AgentDto> result = absWsConsumer.getAgentsApprobateur(9003041);
		setListeAgents(result);
		setTailleListe("5");
		setAfficheAffecterAgent(false);
	}

	@GlobalCommand
	@NotifyChange({ "listeAgents", "listeDelegataire" })
	public void refreshListeAgent() {
		setListeAgents(null);
		setListeDelegataire(null);

		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// on recupere les agents de l'approbateur
			List<AgentDto> result = absWsConsumer.getAgentsApprobateur(9003041);
			setListeAgents(result);
			setAfficheAffecterAgent(false);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les opérateurs de l'agent
			InputterDto result = absWsConsumer.getOperateursDelegataireApprobateur(9003041);
			setListeAgents(result.getOperateurs());
			List<AgentDto> delegataire = null;
			if (result.getDelegataire() != null) {
				delegataire = new ArrayList<>();
				delegataire.add(result.getDelegataire());
			}
			setListeDelegataire(delegataire);
			setAfficheAffecterAgent(true);
		} else if (getTabCourant().getId().equals("VISEUR")) {
			// on recupere les viseurs de l'agent
			ViseursDto result = absWsConsumer.getViseursApprobateur(9003041);
			setListeAgents(result.getViseurs());
			setAfficheAffecterAgent(true);
		}
	}

	@Command
	public void ajouterAgent() {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants", absWsConsumer.getAgentsApprobateur(9003041));
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutAgentApprobateur.zul", null, map);
			win.doModal();
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("operateursExistants", absWsConsumer.getOperateursDelegataireApprobateur(9003041).getOperateurs());
			map.put("delegataireExistants", absWsConsumer.getOperateursDelegataireApprobateur(9003041).getDelegataire());
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutOperateurApprobateur.zul", null,
					map);
			win.doModal();
		} else if (getTabCourant().getId().equals("VISEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("viseursExistants", absWsConsumer.getViseursApprobateur(9003041).getViseurs());
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutViseurApprobateur.zul", null, map);
			win.doModal();
		}
	}

	@Command
	public void affecterAgent(@BindingParam("ref") AgentDto agent) {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants", absWsConsumer.getAgentsOperateursOrViseur(9003041, agent.getIdAgent()));
			map.put("operateur", agent);
			AgentDto approbateur = new AgentDto();
			approbateur.setIdAgent(9003041);
			map.put("approbateur", approbateur);
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutAgentOperateur.zul", null, map);
			win.doModal();
		} else if (getTabCourant().getId().equals("VISEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants", absWsConsumer.getAgentsOperateursOrViseur(9003041, agent.getIdAgent()));
			map.put("viseur", agent);
			AgentDto approbateur = new AgentDto();
			approbateur.setIdAgent(9003041);
			map.put("approbateur", approbateur);
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutAgentViseur.zul", null, map);
			win.doModal();
		}
	}

	@Command
	public void ajouterDelegataire() {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("operateursExistants", absWsConsumer.getOperateursDelegataireApprobateur(9003041).getOperateurs());
			map.put("delegataireExistants", absWsConsumer.getOperateursDelegataireApprobateur(9003041).getDelegataire());
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutDelegataireApprobateur.zul", null,
					map);
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
		} else if (getTabCourant().getId().equals("VISEUR")) {
			supprimerViseursApprobateurs(agentASupprimer);
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

	private void supprimerDelegataireApprobateurs(AgentDto agentDelegataireASupprimer) {
		// on recupere tous le délagatire de l'approbateurs et on supprime
		// l'entree
		if (getListeDelegataire().contains(agentDelegataireASupprimer)) {
			getListeDelegataire().remove(agentDelegataireASupprimer);
		}

		InputterDto dto = new InputterDto();
		dto.setOperateurs(getListeAgents());
		dto.setDelegataire(getListeDelegataire().size() == 0 ? null : getListeDelegataire().get(0));
		ReturnMessageDto result = absWsConsumer.saveOperateursDelegataireApprobateur(9003041, dto);

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

	private void supprimerViseursApprobateurs(AgentDto agentASupprimer) {
		// on recupere tous les viseurs de l'approbateurs et on supprime
		// l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		ViseursDto dto = new ViseursDto();
		dto.setViseurs(getListeAgents());
		ReturnMessageDto result = absWsConsumer.saveViseursApprobateur(9003041, dto);

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Les viseurs ont été enregistrés correctement.");
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
		// on recupere tous les opérateurs de l'approbateurs et on supprime
		// l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		InputterDto dto = new InputterDto();
		dto.setOperateurs(getListeAgents());
		dto.setDelegataire(getListeDelegataire() == null || getListeDelegataire().size() == 0 ? null
				: getListeDelegataire().get(0));
		ReturnMessageDto result = absWsConsumer.saveOperateursDelegataireApprobateur(9003041, dto);

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

	private void supprimerAgentsApprobateurs(AgentDto agentASupprimer) {
		// on recupere tous les agents de l'approbateurs et on supprime l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		ReturnMessageDto result = absWsConsumer.saveAgentsApprobateur(9003041, getListeAgents());

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
	@NotifyChange({ "listeAgents", "listeDelegataire" })
	public void doSearch() {
		List<AgentDto> list = new ArrayList<AgentDto>();
		if (getFilter() != null && !"".equals(getFilter())) {
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
			refreshListeAgent();
		}
	}

	@Command
	public void exportPDF(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfExporter exporter = new PdfExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("droitsAbsence.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	public void exportExcel(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("droitsAbsence.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	@NotifyChange({ "listeAgents", "listeDelegataire" })
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
			List<AgentDto> result = absWsConsumer.getAgentsApprobateur(9003041);
			setListeAgents(result);
			setAfficheAffecterAgent(false);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les opérateurs de l'agent
			InputterDto result = absWsConsumer.getOperateursDelegataireApprobateur(9003041);
			setListeAgents(result.getOperateurs());
			List<AgentDto> delegataire = null;
			if (result.getDelegataire() != null) {
				delegataire = new ArrayList<>();
				delegataire.add(result.getDelegataire());
			}
			setListeDelegataire(delegataire);
			setAfficheAffecterAgent(true);
		} else if (getTabCourant().getId().equals("VISEUR")) {
			// on recupere les viseurs de l'agent
			ViseursDto result = absWsConsumer.getViseursApprobateur(9003041);
			setListeAgents(result.getViseurs());
			setAfficheAffecterAgent(true);
		}
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
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

	public List<AgentDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentDto> listeAgents) {
		this.listeAgents = listeAgents;
	}

	public List<AgentDto> getListeDelegataire() {
		return listeDelegataire;
	}

	public void setListeDelegataire(List<AgentDto> listeDelegataire) {
		this.listeDelegataire = listeDelegataire;
	}

	public boolean isAfficheAffecterAgent() {
		return afficheAffecterAgent;
	}

	public void setAfficheAffecterAgent(boolean afficheAffecterAgent) {
		this.afficheAffecterAgent = afficheAffecterAgent;
	}

}
