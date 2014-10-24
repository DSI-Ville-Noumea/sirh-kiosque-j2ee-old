package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeEtatChangeDto;
import nc.noumea.mairie.kiosque.abs.dto.MotifDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatEnum;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ViserDemandeViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<MotifDto> listeMotifsRefus;

	private String motifRefus;

	private DemandeDto demandeCourant;

	private String avisHierarchique;

	private ProfilAgentDto currentUser;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("demandeCourant") DemandeDto demande) {
		// on recupere la demande selectionnée
		setDemandeCourant(demande);
		setAvisHierarchique(getDemandeCourant().getValeurVisa() == null ? "1"
				: getDemandeCourant().getValeurVisa() ? "1" : "0");
		setMotifRefus(getDemandeCourant().getMotif());
	}

	@Command
	public void saveMotif(@BindingParam("ref") Combobox combo) {
		setMotifRefus(combo.getValue());
	}

	@Command
	@NotifyChange({ "listeMotifsRefus", "motifRefus" })
	public void changeAvis() {
		setListeMotifsRefus(null);
		setMotifRefus(null);
		if (getAvisHierarchique().equals("0")) {
			// on recupere tous les motifs de refus
			List<MotifDto> result = absWsConsumer.getListeMotifsRefus();
			setListeMotifsRefus(result);
		}
	}

	@Command
	public void viseDemande(@BindingParam("win") Window window) {
		if (IsFormValid()) {

			currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

			DemandeEtatChangeDto dto = new DemandeEtatChangeDto();
			dto.setIdDemande(getDemandeCourant().getIdDemande());
			dto.setIdRefEtat(getAvisHierarchique().equals("0") ? RefEtatEnum.VISEE_DEFAVORABLE.getCodeEtat()
					: RefEtatEnum.VISEE_FAVORABLE.getCodeEtat());
			dto.setDateAvis(new Date());
			dto.setMotif(getMotifRefus());

			ReturnMessageDto result = absWsConsumer.changerEtatDemandeAbsence(currentUser.getAgent().getIdAgent(), dto);

			if (result.getErrors().size() > 0 || result.getInfos().size() > 0) {
				final HashMap<String, Object> map = new HashMap<String, Object>();
				List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
				List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
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
					BindUtils.postGlobalCommand(null, null, "refreshListeDemande", null);
					window.detach();
				}
			}
		}
	}

	private boolean IsFormValid() {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Motif
		if (getMotifRefus() == null && getAvisHierarchique().equals("0")) {
			vList.add(new ValidationMessage("Le motif est obligatoire."));
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	public String getMotifRefus() {
		return motifRefus;
	}

	public void setMotifRefus(String motifRefus) {
		this.motifRefus = motifRefus;
	}

	public DemandeDto getDemandeCourant() {
		return demandeCourant;
	}

	public void setDemandeCourant(DemandeDto demandeCourant) {
		this.demandeCourant = demandeCourant;
	}

	public String getAvisHierarchique() {
		return avisHierarchique;
	}

	public void setAvisHierarchique(String avisHierarchique) {
		this.avisHierarchique = avisHierarchique;
	}

	public List<MotifDto> getListeMotifsRefus() {
		return listeMotifsRefus;
	}

	public void setListeMotifsRefus(List<MotifDto> listeMotifsRefus) {
		this.listeMotifsRefus = listeMotifsRefus;
	}
}
