package nc.noumea.mairie.kiosque.ptg.droits.viewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DroitsAccesViewModel extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private List<AgentDto> listeAgents;

	private List<AgentDto> listeDelegataire;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private boolean afficheAffecterAgent;
	private Tab tabCourant;

	private ProfilAgentDto currentUser;

	@Init
	public void initDroitsAccesPointage() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		AccessRightsPtgDto accessRightsDto = ptgWsConsumer.getListAccessRightsByAgent(currentUser.getAgent()
				.getIdAgent());

		if (!accessRightsDto.isGestionDroitsAcces()) {
			Executions.getCurrent().sendRedirect("401.jsp");
		}

		setListeAgents(ptgWsConsumer.getApprovedAgents(currentUser.getAgent().getIdAgent()));

		setTailleListe("5");
	}

	@Command
	public void ajouterAgent() {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants", ptgWsConsumer.getApprovedAgents(currentUser.getAgent().getIdAgent()));
			Window win = (Window) Executions.createComponents("/pointages/droits/ajoutAgentApprobateur.zul", null, map);
			win.doModal();
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("operateursExistants", ptgWsConsumer.getDelegateAndOperator(currentUser.getAgent().getIdAgent())
					.getSaisisseurs());
			map.put("delegataireExistants", ptgWsConsumer.getDelegateAndOperator(currentUser.getAgent().getIdAgent())
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
			List<AgentDto> result = ptgWsConsumer.getApprovedAgents(currentUser.getAgent().getIdAgent());
			setListeAgents(result);
			setAfficheAffecterAgent(false);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les op�rateurs de l'agent
			DelegatorAndOperatorsDto result = ptgWsConsumer.getDelegateAndOperator(currentUser.getAgent().getIdAgent());
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
			List<AgentDto> result = ptgWsConsumer.getApprovedAgents(currentUser.getAgent().getIdAgent());
			setListeAgents(result);
			setAfficheAffecterAgent(false);
		} else if (getTabCourant().getId().equals("OPERATEUR")) {
			// on recupere les op�rateurs de l'agent
			DelegatorAndOperatorsDto result = ptgWsConsumer.getDelegateAndOperator(currentUser.getAgent().getIdAgent());
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
					ptgWsConsumer.getAgentsSaisisOperateur(currentUser.getAgent().getIdAgent(), agent.getIdAgent()));
			map.put("operateur", agent);
			AgentDto approbateur = new AgentDto();
			approbateur.setIdAgent(currentUser.getAgent().getIdAgent());
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
			map.put("operateursExistants", ptgWsConsumer.getDelegateAndOperator(currentUser.getAgent().getIdAgent())
					.getSaisisseurs());
			map.put("delegataireExistants", ptgWsConsumer.getDelegateAndOperator(currentUser.getAgent().getIdAgent())
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

	private void supprimerDelegataireApprobateurs(AgentDto agentDelegataireASupprimer) {
		// on recupere tous le d�lagatire de l'approbateurs et on supprime
		// l'entree
		if (getListeDelegataire().contains(agentDelegataireASupprimer)) {
			getListeDelegataire().remove(agentDelegataireASupprimer);
		}

		DelegatorAndOperatorsDto dto = new DelegatorAndOperatorsDto();
		dto.setSaisisseurs(getListeAgents());
		dto.setDelegataire(getListeDelegataire().size() == 0 ? null : getListeDelegataire().get(0));
		ReturnMessageDto result = ptgWsConsumer.saveDelegateAndOperator(currentUser.getAgent().getIdAgent(), dto);

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

	private void supprimerOperateursApprobateurs(AgentDto agentASupprimer) {
		// on recupere tous les op�rateurs de l'approbateurs et on supprime
		// l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		DelegatorAndOperatorsDto dto = new DelegatorAndOperatorsDto();
		dto.setSaisisseurs(getListeAgents());
		dto.setDelegataire(getListeDelegataire() == null || getListeDelegataire().size() == 0 ? null
				: getListeDelegataire().get(0));
		ReturnMessageDto result = ptgWsConsumer.saveDelegateAndOperator(currentUser.getAgent().getIdAgent(), dto);

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

		ReturnMessageDto result = ptgWsConsumer.saveApprovedAgents(currentUser.getAgent().getIdAgent(),
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

	public List<AgentDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentDto> listeAgents) {
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

}
