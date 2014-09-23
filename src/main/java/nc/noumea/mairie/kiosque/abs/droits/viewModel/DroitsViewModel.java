package nc.noumea.mairie.kiosque.abs.droits.viewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
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

	private Tab tabCourant;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initDroits() {
		// on recupere les agents de l'approbateur
		List<AgentDto> result = absWsConsumer.getAgentsApprobateur(9005138);
		setListeAgents(result);
		setTailleListe("5");
	}

	@GlobalCommand
	@NotifyChange({ "listeAgents" })
	public void refreshListeAgent() {
		// on recupere les agents de l'approbateur
		List<AgentDto> result = absWsConsumer.getAgentsApprobateur(9005138);
		setListeAgents(result);
	}

	@Listen("onClick = #AJOUTER")
	public void ajouterAgentApprobateur(Event e) {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			// create a window programmatically and use it as a modal dialog.
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("agentsExistants", absWsConsumer.getAgentsApprobateur(9005138));
			Window win = (Window) Executions.createComponents("/absences/droits/ajoutAgentApprobateur.zul", null, map);
			win.doModal();
		}
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void supprimerAgent(@BindingParam("ref") AgentDto agentASupprimer) {
		// on regarde dans quel onglet on est
		if (getTabCourant().getId().equals("APPROBATEUR")) {
			supprimerAgentsApprobateurs(agentASupprimer);
		}
	}

	private void supprimerAgentsApprobateurs(AgentDto agentASupprimer) {
		// on recupere tous les agnts de l'approbateurs et on supprime l'entree
		if (getListeAgents().contains(agentASupprimer)) {
			getListeAgents().remove(agentASupprimer);
		}

		ReturnMessageDto result = absWsConsumer.saveAgentsApprobateur(9005138, getListeAgents());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
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
	@NotifyChange({ "listeAgents" })
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
			List<AgentDto> result = absWsConsumer.getAgentsApprobateur(9005138);
			setListeAgents(result);
		}
	}

	@Command
	public void exportPDF(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfExporter exporter = new PdfExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("agentsApprouver.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	public void exportExcel(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("agentsApprouver.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		// on sauvegarde l'onglet
		setTabCourant(tab);
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

}
