package nc.noumea.mairie.kiosque.abs.droits.viewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.InputterDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutOperateurApprobateurViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private List<AgentDto> listeAgents;

	private List<AgentDto> listeAgentsExistants;

	private List<AgentDto> listeDelegataireExistants;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;
	
	private ProfilAgentDto currentUser;

	@Init
	public void initAjoutOperateur(@ExecutionArgParam("operateursExistants") List<AgentDto> operateursExistants,
			@ExecutionArgParam("delegataireExistants") AgentDto delegataireExistants) {
		
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		
		// on sauvegarde qui sont les opérateurs de l'approbateur
		setListeAgentsExistants(operateursExistants);
		// on sauvegarde qui est le delegataire de l'approbateur
		List<AgentDto> delegataire = null;
		if (delegataireExistants != null) {
			delegataire = new ArrayList<>();
			delegataire.add(delegataireExistants);
		}
		setListeDelegataireExistants(delegataire);
		// on vide
		viderZones();
		// on charge tous les agents de la mairie
		List<AgentWithServiceDto> result = sirhWsConsumer.getListeAgentsMairie();
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
		InputterDto dto = new InputterDto();
		dto.setOperateurs(listSelect);
		dto.setDelegataire(getListeDelegataireExistants() == null || getListeDelegataireExistants().size() == 0 ? null
				: getListeDelegataireExistants().get(0));
		ReturnMessageDto result = absWsConsumer.saveOperateursDelegataireApprobateur(currentUser.getAgent().getIdAgent(), dto);

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
			List<AgentWithServiceDto> result = sirhWsConsumer.getListeAgentsMairie();
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

	public List<AgentDto> getListeDelegataireExistants() {
		return listeDelegataireExistants;
	}

	public void setListeDelegataireExistants(List<AgentDto> listeDelegataireExistants) {
		this.listeDelegataireExistants = listeDelegataireExistants;
	}
}
