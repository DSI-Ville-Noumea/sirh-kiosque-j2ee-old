package nc.noumea.mairie.kiosque.abs.droits.viewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ViseursDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutViseurApprobateurViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<AgentDto> listeAgents;

	private List<AgentDto> listeAgentsExistants;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initDemandes(@ExecutionArgParam("viseursExistants") List<AgentDto> viseursExistants) {
		// on sauvegarde qui sont les opérateurs de l'approbateur
		setListeAgentsExistants(viseursExistants);
		// on vide
		viderZones();
		// on charge les sous agents
		List<AgentWithServiceDto> result = sirhWsConsumer.getAgentEquipe(9005138, null);
		setListeAgents(transformeListe(result));
		setTailleListe("5");
	}

	@Command
	public void saveAgent(@BindingParam("win") Window window, @BindingParam("listBox") Listbox listAgent) {
		// on récupère les agents selectionnés
		List<Listitem> t = listAgent.getItems();
		List<AgentDto> listSelect = new ArrayList<AgentDto>();
		for (Listitem a : t) {
			if (a.isSelected())
				listSelect.add((AgentDto) a.getValue());
		}
		ViseursDto dto = new ViseursDto();
		dto.setViseurs(listSelect);
		ReturnMessageDto result = absWsConsumer.saveViseursApprobateur(9005138, dto);

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
			BindUtils.postGlobalCommand(null, null, "refreshListeAgent", null);
			window.detach();
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
			List<AgentWithServiceDto> result = sirhWsConsumer.getAgentEquipe(9005138, null);
			setListeAgents(transformeListe(result));
		}
	}

	private List<AgentDto> transformeListe(List<AgentWithServiceDto> result) {
		List<AgentDto> listFinale = new ArrayList<AgentDto>();
		for (AgentWithServiceDto agDto : result) {
			AgentDto dto = new AgentDto(agDto);
			dto.setSelectedDroitAbs(getListeAgentsExistants().contains(dto));
			listFinale.add(dto);
		}
		return listFinale;
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	private void viderZones() {
		setListeAgents(null);
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

	public List<AgentDto> getListeAgentsExistants() {
		return listeAgentsExistants;
	}

	public void setListeAgentsExistants(List<AgentDto> listeAgentsExistants) {
		this.listeAgentsExistants = listeAgentsExistants;
	}
}
