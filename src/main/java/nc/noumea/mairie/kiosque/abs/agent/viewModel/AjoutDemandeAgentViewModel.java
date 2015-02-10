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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.kiosque.viewModel.TimePicker;
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

	// POUR LA GESTION DES HEURES
	private List<String> listeHeures;
	private List<String> listeMinutes;

	private String heureDebut;
	private String minuteDebut;
	private String heureFin;
	private String minuteFin;
	private String dureeHeureDemande;
	private String dureeMinuteDemande;

	@Init
	public void initAjoutDemandeAgent() {
		// on vide
		viderZones();

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on recharge les groupes d'absences pour les filtres
		List<RefGroupeAbsenceDto> filtreGroupeFamille = absWsConsumer.getRefGroupeAbsence();
		setListeGroupeAbsence(filtreGroupeFamille);
		// on positionne la selection du statut Provisoire/Définitif
		setEtatDemandeCreation("0");
		// on initialise la demande
		setDemandeCreation(new DemandeDto());

		// minutes et heures
		TimePicker timePicker = new TimePicker();
		setListeMinutes(timePicker.getListeMinutes());
		setListeHeures(timePicker.getListeHeures());
	}

	@Command
	@NotifyChange({ "listeTypeAbsence", "typeAbsenceCourant", "dureeHeureDemande", "dureeMinuteDemande", "." })
	public void alimenteTypeFamilleAbsence() {
		List<RefTypeAbsenceDto> filtreFamilleAbsence = absWsConsumer.getRefTypeAbsenceKiosque(getGroupeAbsence()
				.getIdRefGroupeAbsence(), currentUser.getAgent().getIdAgent());

		setListeTypeAbsence(filtreFamilleAbsence);
		if (getListeTypeAbsence().size() == 1) {
			setTypeAbsenceCourant(getListeTypeAbsence().get(0));
			setDureeHeureDemande(null);
			setDureeMinuteDemande(null);
		} else {
			setTypeAbsenceCourant(null);
			setDureeHeureDemande(null);
			setDureeMinuteDemande(null);
		}
	}

	@Command
	@NotifyChange({ "listeOrganisationsSyndicale", "organisationsSyndicaleCourant", "." })
	public void alimenteOrganisation() {
		if (getTypeAbsenceCourant() != null && getTypeAbsenceCourant().getTypeSaisiDto() != null
				&& getTypeAbsenceCourant().getTypeSaisiDto().isCompteurCollectif()) {
			// on recharge les organisations syndicales
			List<OrganisationSyndicaleDto> orga = absWsConsumer.getListOrganisationSyndicale(currentUser.getAgent()
					.getIdAgent(), getTypeAbsenceCourant().getIdRefTypeAbsence());
			if (orga.size() == 0) {
				OrganisationSyndicaleDto dto = new OrganisationSyndicaleDto();
				dto.setLibelle("L'agent n'est affecté à aucune organisation syndicale");
				dto.setSigle("ERREUR");
				orga.add(dto);
			} else if (orga.size() == 1) {
				setOrganisationsSyndicaleCourant(orga.get(0));
			}

			setListeOrganisationsSyndicale(orga);
		}
	}

	public String getCalculDureeCongeAnnuel(String codeBaseHoraireAbsence, DemandeDto demandeDto) {
		if (demandeDto.getDateDebut() != null && demandeDto.getDateFin() != null) {
			demandeDto.setTypeSaisiCongeAnnuel(getTypeAbsenceCourant().getTypeSaisiCongeAnnuelDto());
			AgentWithServiceDto agentWithServiceDto = new AgentWithServiceDto();
			agentWithServiceDto.setIdAgent(currentUser.getAgent().getIdAgent());
			demandeDto.setAgentWithServiceDto(agentWithServiceDto);
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
		setDureeHeureDemande(null);
		setDureeMinuteDemande(null);
	}

	@Command
	public void saveDemande(@BindingParam("win") Window window) throws ParseException {

		if (IsFormValid(getTypeAbsenceCourant())) {

			if (getTypeAbsenceCourant().getTypeSaisiDto() != null) {
				if (getTypeAbsenceCourant().getTypeSaisiDto().isCalendarHeureDebut()) {
					// recup heure debut
					Calendar calDebut = Calendar.getInstance();
					calDebut.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
					calDebut.setTime(getDemandeCreation().getDateDebut());
					calDebut.set(Calendar.HOUR, Integer.valueOf(getHeureDebut()));
					calDebut.set(Calendar.MINUTE, Integer.valueOf(getMinuteDebut()));
					calDebut.set(Calendar.SECOND, 0);

					getDemandeCreation().setDateDebut(calDebut.getTime());
				}
				if (getTypeAbsenceCourant().getTypeSaisiDto().isCalendarHeureFin()) {
					// recup heure fin
					Calendar calFin = Calendar.getInstance();
					calFin.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
					calFin.setTime(getDemandeCreation().getDateDebut());
					calFin.set(Calendar.HOUR, Integer.valueOf(getHeureFin()));
					calFin.set(Calendar.MINUTE, Integer.valueOf(getMinuteFin()));
					calFin.set(Calendar.SECOND, 0);

					getDemandeCreation().setDateFin(calFin.getTime());
				}
				// duree
				if (getTypeAbsenceCourant().getTypeSaisiDto().isDuree()) {
					String dureeTotale = getDureeHeureDemande() + "." + getDureeMinuteDemande();
					getDemandeCreation().setDuree(Double.valueOf(dureeTotale));
				}
			}

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
		getDemandeCreation().setTypeSaisi(getTypeAbsenceCourant().getTypeSaisiDto());
		getDemandeCreation().setTypeSaisiCongeAnnuel(getTypeAbsenceCourant().getTypeSaisiCongeAnnuelDto());
		if (getDemandeCreation().getTypeSaisiCongeAnnuel() != null) {
			if (null == getDemandeCreation().getDateFin()
					&& getDemandeCreation().getTypeSaisiCongeAnnuel().isCalendarDateFin()) {
				getDemandeCreation().setDateFin(getDemandeCreation().getDateDebut());
			}
			if (null == getDemandeCreation().getDateFin()
					&& getDemandeCreation().getTypeSaisiCongeAnnuel().isCalendarDateReprise()) {
				Calendar calReprise = Calendar.getInstance();
				calReprise.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
				calReprise.setTime(getDemandeCreation().getDateDebut());
				calReprise.add(Calendar.DAY_OF_MONTH, 1);
				getDemandeCreation().setDateReprise(calReprise.getTime());
				getDemandeCreation().setDateFin(calReprise.getTime());
			}
			refreshDuree();
		}
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
			// heure debut
			if (refTypeAbsenceDto.getTypeSaisiDto().isCalendarHeureDebut()) {
				if (getHeureDebut() == null || getMinuteDebut() == null) {
					vList.add(new ValidationMessage("Merci de choisir une heure de début."));
				}
			}
			// heure fin
			if (refTypeAbsenceDto.getTypeSaisiDto().isCalendarHeureFin()) {
				if (getHeureFin() == null || getMinuteFin() == null) {
					vList.add(new ValidationMessage("Merci de choisir une heure de fin."));
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
				if (getDureeHeureDemande() == null || getDureeMinuteDemande() == null) {
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
				if (getDemandeCreation().getDateReprise() == null) {
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

	public List<String> getListeHeures() {
		return listeHeures;
	}

	public void setListeHeures(List<String> listeHeures) {
		this.listeHeures = listeHeures;
	}

	public List<String> getListeMinutes() {
		return listeMinutes;
	}

	public void setListeMinutes(List<String> listeMinutes) {
		this.listeMinutes = listeMinutes;
	}

	public String getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(String heureDebut) {
		this.heureDebut = heureDebut;
	}

	public String getMinuteDebut() {
		return minuteDebut;
	}

	public void setMinuteDebut(String minuteDebut) {
		this.minuteDebut = minuteDebut;
	}

	public String getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(String heureFin) {
		this.heureFin = heureFin;
	}

	public String getMinuteFin() {
		return minuteFin;
	}

	public void setMinuteFin(String minuteFin) {
		this.minuteFin = minuteFin;
	}

	public boolean getAfficherValider() {
		if (getTypeAbsenceCourant() == null) {
			return false;
		} else if (getTypeAbsenceCourant().getTypeSaisiCongeAnnuelDto() == null
				&& getTypeAbsenceCourant().getTypeSaisiDto() == null) {
			return false;
		}
		return true;
	}

	public String getDureeHeureDemande() {
		return dureeHeureDemande;
	}

	public void setDureeHeureDemande(String dureeHeureDemande) {
		this.dureeHeureDemande = dureeHeureDemande;
	}

	public String getDureeMinuteDemande() {
		return dureeMinuteDemande;
	}

	public void setDureeMinuteDemande(String dureeMinuteDemande) {
		this.dureeMinuteDemande = dureeMinuteDemande;
	}
}
