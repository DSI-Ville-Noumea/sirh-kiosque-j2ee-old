package nc.noumea.mairie.kiosque.abs.agent.viewModel;

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

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutDemandeAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private DemandeDto demandeCreation;

	private List<RefGroupeAbsenceDto> listeGroupeAbsence;
	private RefGroupeAbsenceDto groupeAbsence;

	private List<RefTypeAbsenceDto> listeTypeAbsence;
	private RefTypeAbsenceDto typeAbsenceCourant;

	private String etatDemandeCreation;
	private String dureeCongeAnnuel;

	private List<OrganisationSyndicaleDto> listeOrganisationsSyndicale;

	private OrganisationSyndicaleDto organisationsSyndicaleCourant;

	// pour savoir si la date de debut est le matin
	private String selectDebutAM;
	// pour savoir si la date de fin est le matin
	private String selectFinAM;

	private ProfilAgentDto currentUser;

	@Init
	public void initAjoutDemandeAgent() {
		// on vide
		viderZones();

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on recharge les groupes d'absences pour les filtres
		List<RefGroupeAbsenceDto> filtreGroupeFamille = absWsConsumer.getRefGroupeAbsence();
		setListeGroupeAbsence(filtreGroupeFamille);

		// on recharge les oragnisations syndicales
		List<OrganisationSyndicaleDto> orga = absWsConsumer.getListOrganisationSyndicale();
		setListeOrganisationsSyndicale(orga);
		// on positionne la selection du statut Provisoire/Définitif
		setEtatDemandeCreation("0");
		// on initialise la demande
		setDemandeCreation(new DemandeDto());
	}

	@Command
	@NotifyChange({ "listeTypeAbsence", "typeAbsenceCourant" })
	public void alimenteTypeFamilleAbsence() {
		List<RefTypeAbsenceDto> filtreFamilleAbsence = absWsConsumer.getRefTypeAbsenceKiosque(getGroupeAbsence()
				.getIdRefGroupeAbsence(), currentUser.getAgent().getIdAgent());

		setListeTypeAbsence(filtreFamilleAbsence);
		setTypeAbsenceCourant(null);
	}

	public String getCalculDureeCongeAnnuel(String codeBaseHoraireAbsence, DemandeDto demandeDto) {
		if (demandeDto.getDateDebut() != null && demandeDto.getDateFin() != null) {
			demandeDto.setTypeSaisiCongeAnnuel(getTypeAbsenceCourant().getTypeSaisiCongeAnnuelDto());
			DemandeDto dureeDto = absWsConsumer.getDureeCongeAnnuel(demandeDto);
			return dureeDto.getDuree().toString();
		}
		return null;
	}

	@Command
	@NotifyChange({ "dureeCongeAnnuel" })
	public void refreshDuree() {
		getDemandeCreation().setDateDebutAM(
				getSelectDebutAM() == null ? false : getSelectDebutAM().equals("AM") ? true : false);
		getDemandeCreation().setDateDebutPM(
				getSelectDebutAM() == null ? false : getSelectDebutAM().equals("PM") ? true : false);
		getDemandeCreation().setDateFinAM(
				getSelectFinAM() == null ? false : getSelectFinAM().equals("AM") ? true : false);
		getDemandeCreation().setDateFinPM(
				getSelectFinAM() == null ? false : getSelectFinAM().equals("PM") ? true : false);
		setDureeCongeAnnuel(getCalculDureeCongeAnnuel(getTypeAbsenceCourant().getTypeSaisiCongeAnnuelDto()
				.getCodeBaseHoraireAbsence(), getDemandeCreation()));
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	private void viderZones() {
		setDemandeCreation(null);
		setListeGroupeAbsence(null);
		setGroupeAbsence(null);
		setListeTypeAbsence(null);
		setTypeAbsenceCourant(null);
		setEtatDemandeCreation(null);
		setListeOrganisationsSyndicale(null);
		setOrganisationsSyndicaleCourant(null);
		setSelectDebutAM(null);
		setSelectFinAM(null);
	}

	@Command
	public void saveDemande(@BindingParam("win") Window window) {

		if (IsFormValid(getTypeAbsenceCourant())) {
			AgentWithServiceDto agentWithServiceDto = new AgentWithServiceDto();
			agentWithServiceDto.setIdAgent(currentUser.getAgent().getIdAgent());

			getDemandeCreation().setIdRefEtat(Integer.valueOf(getEtatDemandeCreation()));
			getDemandeCreation().setIdTypeDemande(getTypeAbsenceCourant().getIdRefTypeAbsence());
			getDemandeCreation().setGroupeAbsence(getTypeAbsenceCourant().getGroupeAbsence());
			getDemandeCreation().setTypeSaisi(getTypeAbsenceCourant().getTypeSaisiDto());
			getDemandeCreation().setTypeSaisiCongeAnnuel(getTypeAbsenceCourant().getTypeSaisiCongeAnnuelDto());
			getDemandeCreation().setAgentWithServiceDto(agentWithServiceDto);
			getDemandeCreation().setOrganisationSyndicale(getOrganisationsSyndicaleCourant());
			getDemandeCreation().setDateDebutAM(
					getSelectDebutAM() == null ? false : getSelectDebutAM().equals("AM") ? true : false);
			getDemandeCreation().setDateDebutPM(
					getSelectDebutAM() == null ? false : getSelectDebutAM().equals("PM") ? true : false);
			getDemandeCreation().setDateFinAM(
					getSelectFinAM() == null ? false : getSelectFinAM().equals("AM") ? true : false);
			getDemandeCreation().setDateFinPM(
					getSelectFinAM() == null ? false : getSelectFinAM().equals("PM") ? true : false);
			if (getDemandeCreation().getTypeSaisi() != null
					&& getDemandeCreation().getTypeSaisi().getUniteDecompte().equals("minutes")) {
				getDemandeCreation().setDuree(getDemandeCreation().getDuree() * 60);
			}

			ReturnMessageDto result = absWsConsumer.saveDemandeAbsence(currentUser.getAgent().getIdAgent(),
					getDemandeCreation());

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

	@Command
	@NotifyChange({ "demandeCreation", "dureeCongeAnnuel" })
	public void alimenteDateFin() {
		if (null == getDemandeCreation().getDateFin()) {
			getDemandeCreation().setDateFin(getDemandeCreation().getDateDebut());
		}
		refreshDuree();
	}

	private boolean IsFormValid(RefTypeAbsenceDto refTypeAbsenceDto) {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Date de debut
		if (getDemandeCreation().getDateDebut() == null) {
			vList.add(new ValidationMessage("La date de début est obligatoire."));
		}
		if (refTypeAbsenceDto.getTypeSaisiDto() != null) {
			if (refTypeAbsenceDto.getTypeSaisiDto().isChkDateDebut()) {
				if (getSelectDebutAM() == null) {
					vList.add(new ValidationMessage("Merci de choisir M/AM pour la date de début."));
				}
			}

			// OS
			if (refTypeAbsenceDto.getTypeSaisiDto().isCompteurCollectif()) {
				if (getOrganisationsSyndicaleCourant() == null) {
					vList.add(new ValidationMessage("L'organisation syndicale est obligatoire."));
				}
			}

			// DUREE
			if (refTypeAbsenceDto.getTypeSaisiDto().isDuree()) {
				if (getDemandeCreation().getDuree() == null || getDemandeCreation().getDuree() == 0) {
					vList.add(new ValidationMessage("La durée est obligatoire."));
				}
			}

			// DATE FIN
			if (refTypeAbsenceDto.getTypeSaisiDto().isCalendarDateFin()) {
				if (getDemandeCreation().getDateFin() == null) {
					vList.add(new ValidationMessage("La date de fin est obligatoire."));
				}
			}
			if (refTypeAbsenceDto.getTypeSaisiDto().isChkDateFin()) {
				if (getSelectFinAM() == null) {
					vList.add(new ValidationMessage("Merci de choisir M/AM pour la date de fin."));
				}
			}

			// MOTIF
			if (refTypeAbsenceDto.getTypeSaisiDto().isMotif()) {
				if (getDemandeCreation().getCommentaire() == null) {
					vList.add(new ValidationMessage("Le motif est obligatoire."));
				}
			}
		} else if (refTypeAbsenceDto.getTypeSaisiCongeAnnuelDto() != null) {
			if (refTypeAbsenceDto.getTypeSaisiCongeAnnuelDto().isChkDateDebut()) {
				if (getSelectDebutAM() == null) {
					vList.add(new ValidationMessage("Merci de choisir M/AM pour la date de début."));
				}
			}

			// DATE FIN
			if (refTypeAbsenceDto.getTypeSaisiCongeAnnuelDto().isCalendarDateFin()) {
				if (getDemandeCreation().getDateFin() == null) {
					vList.add(new ValidationMessage("La date de fin est obligatoire."));
				}
			}
			if (refTypeAbsenceDto.getTypeSaisiCongeAnnuelDto().isChkDateFin()) {
				if (getSelectFinAM() == null) {
					vList.add(new ValidationMessage("Merci de choisir M/AM pour la date de fin."));
				}
			}

			// DATE REPRISE
			if (refTypeAbsenceDto.getTypeSaisiCongeAnnuelDto().isCalendarDateReprise()) {
				if (getDemandeCreation().getDateFin() == null) {
					vList.add(new ValidationMessage("La date de reprise est obligatoire."));
				}
			}
		} else {
			vList.add(new ValidationMessage(
					"Une erreur est survenue dans l'enregistrement de la demande. Merci de recommencer."));
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}

	public List<RefTypeAbsenceDto> getListeTypeAbsence() {
		return listeTypeAbsence;
	}

	public void setListeTypeAbsence(List<RefTypeAbsenceDto> listeTypeAbsence) {
		this.listeTypeAbsence = listeTypeAbsence;
	}

	public RefTypeAbsenceDto getTypeAbsenceCourant() {
		return typeAbsenceCourant;
	}

	public void setTypeAbsenceCourant(RefTypeAbsenceDto typeAbsenceCourant) {
		this.typeAbsenceCourant = typeAbsenceCourant;
	}

	public DemandeDto getDemandeCreation() {
		return demandeCreation;
	}

	public void setDemandeCreation(DemandeDto demandeCreation) {
		this.demandeCreation = demandeCreation;
	}

	public String getEtatDemandeCreation() {
		return etatDemandeCreation;
	}

	public void setEtatDemandeCreation(String etatDemandeCreation) {
		this.etatDemandeCreation = etatDemandeCreation;
	}

	public List<OrganisationSyndicaleDto> getListeOrganisationsSyndicale() {
		return listeOrganisationsSyndicale;
	}

	public void setListeOrganisationsSyndicale(List<OrganisationSyndicaleDto> listeOrganisationsSyndicale) {
		this.listeOrganisationsSyndicale = listeOrganisationsSyndicale;
	}

	public OrganisationSyndicaleDto getOrganisationsSyndicaleCourant() {
		return organisationsSyndicaleCourant;
	}

	public void setOrganisationsSyndicaleCourant(OrganisationSyndicaleDto organisationsSyndicaleCourant) {
		this.organisationsSyndicaleCourant = organisationsSyndicaleCourant;
	}

	public String getSelectDebutAM() {
		return selectDebutAM;
	}

	public void setSelectDebutAM(String selectDebutAM) {
		this.selectDebutAM = selectDebutAM;
	}

	public String getSelectFinAM() {
		return selectFinAM;
	}

	public void setSelectFinAM(String selectFinAM) {
		this.selectFinAM = selectFinAM;
	}

	public List<RefGroupeAbsenceDto> getListeGroupeAbsence() {
		return listeGroupeAbsence;
	}

	public void setListeGroupeAbsence(List<RefGroupeAbsenceDto> listeGroupeAbsence) {
		this.listeGroupeAbsence = listeGroupeAbsence;
	}

	public RefGroupeAbsenceDto getGroupeAbsence() {
		return groupeAbsence;
	}

	public void setGroupeAbsence(RefGroupeAbsenceDto groupeAbsence) {
		this.groupeAbsence = groupeAbsence;
	}

	public String getDureeCongeAnnuel() {
		return dureeCongeAnnuel;
	}

	public void setDureeCongeAnnuel(String dureeCongeAnnuel) {
		this.dureeCongeAnnuel = dureeCongeAnnuel;
	}
}
