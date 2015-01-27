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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ChangerDelegataireViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private String titre;

	private List<AgentDto> listeDelegataire;

	private AgentDto delegataire;

	private EaeListItemDto eaeCourant;

	@Init
	public void initTableauEae(@ExecutionArgParam("eae") EaeListItemDto eae) {
		setTitre("Veuillez selectionner le délégataire de " + eae.getAgentEvalue().getNom() + " "
				+ eae.getAgentEvalue().getPrenom());
		setEaeCourant(eae);
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupère la liste des délégataires possibles
		List<AgentDto> listDelegataire = sirhWsConsumer.getListDelegataire(currentUser.getAgent().getIdAgent());
		setListeDelegataire(listDelegataire);

	}

	@Command
	public void saveDelegataire(@BindingParam("win") Window window) {
		ReturnMessageDto result = eaeWsConsumer.saveDelegataire(getEaeCourant().getIdEae(), currentUser.getAgent()
				.getIdAgent(), getDelegataire().getIdAgent());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
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
			BindUtils.postGlobalCommand(null, null, "filtrer", null);
			window.detach();
		}

	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public List<AgentDto> getListeDelegataire() {
		return listeDelegataire;
	}

	public void setListeDelegataire(List<AgentDto> listeDelegataire) {
		this.listeDelegataire = listeDelegataire;
	}

	public AgentDto getDelegataire() {
		return delegataire;
	}

	public void setDelegataire(AgentDto delegataire) {
		this.delegataire = delegataire;
	}

	public EaeListItemDto getEaeCourant() {
		return eaeCourant;
	}

	public void setEaeCourant(EaeListItemDto eaeCourant) {
		this.eaeCourant = eaeCourant;
	}
}
