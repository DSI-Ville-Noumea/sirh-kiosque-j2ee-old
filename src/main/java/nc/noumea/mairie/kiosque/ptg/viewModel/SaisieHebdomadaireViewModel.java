package nc.noumea.mairie.kiosque.ptg.viewModel;

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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AbsenceDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.HeureSupDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.MotifHeureSupDto;
import nc.noumea.mairie.kiosque.ptg.dto.PrimeDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.ServiceDto;
import nc.noumea.mairie.kiosque.ptg.form.SaisiePointageForm;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.kiosque.viewModel.TimePicker;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SaisieHebdomadaireViewModel extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private ProfilAgentDto currentUser;

	private FichePointageDtoKiosque ficheCourante;

	private SaisiePointageForm saisiePointageForm;

	/* Pour savoir si on affiche la disquette de sauvegarde */
	private boolean hasTextChanged;

	/* POUR LES FILTRES */
	private Date dateLundi;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private String dateFiltre;

	private ListModel<RefTypeAbsenceDto> listeTypeAbsence;
	private ListModel<MotifHeureSupDto> listeMotifHsup;

	private List<String> listeHeures;
	private List<String> listeMinutes;
	
	private boolean checkCoche;

	@Init
	public void initSaisieFichePointage(@ExecutionArgParam("pointage") ConsultPointageDto pointage)
			throws ParseException {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		// si 1 seul service alors on le selectionne
		if (getListeServicesFiltre() != null && getListeServicesFiltre().size() == 1) {
			setServiceFiltre(getListeServicesFiltre().get(0));
			afficheListeAgent();
		}
		setDateFiltre("Semaine ... du ... au ...");

		setListeTypeAbsence(getModelTypeAbsence());
		setListeMotifHsup(getModelMotifHsup());

		if (pointage != null) {
			// c'est qu'on vient de l'ecran de visu des pointages
			setDateLundi(pointage.getDate());
			afficheSemaine();
			AgentWithServiceDto agent = sirhWsConsumer
					.getAgentService(pointage.getAgent().getIdAgent(), getDateLundi());
			ServiceDto servAgent = new ServiceDto();
			servAgent.setCodeService(agent.getCodeService());
			servAgent.setService(agent.getService());
			setServiceFiltre(servAgent);
			afficheListeAgent();
			setAgentFiltre(pointage.getAgent());
			chargeFiche();
		}
	}

	private ListModel<MotifHeureSupDto> getModelMotifHsup() {

		return new ListModelList<MotifHeureSupDto>(ptgWsConsumer.getListeMotifHsup());
	}

	private ListModel<RefTypeAbsenceDto> getModelTypeAbsence() {

		return new ListModelList<RefTypeAbsenceDto>(ptgWsConsumer.getListeRefTypeAbsence());
	}

	@Command
	@NotifyChange({ "hasTextChanged" })
	public void textChanged() {
		setHasTextChanged(true);
	}

	@Command
	public void textChangedPrime(@BindingParam("ref") PrimeDtoKiosque dtoPrime,
			@BindingParam("heureDebut") String heureDebut, @BindingParam("minuteDebut") String minuteDebut,
			@BindingParam("heureFin") String heureFin, @BindingParam("minuteFin") String minuteFin) {
		setHasTextChanged(true);
		FichePointageDtoKiosque dto = getFicheCourante();
		if (heureDebut != null && minuteDebut != null && heureFin != null && minuteFin != null) {
			// les primes
			// on mappe
			if (null != getSaisiePointageForm().getMapAllPrime()) {
				Map<String, List<PrimeDtoKiosque>> mapAllPrime = getSaisiePointageForm().getMapAllPrime();

				for (String key : mapAllPrime.keySet()) {
					List<PrimeDtoKiosque> listPrimes = mapAllPrime.get(key);
					int iJour = 0;
					for (PrimeDtoKiosque primeDto : listPrimes) {
						if (null != primeDto.getIdRefEtat()) {
							if (periodeHeure(primeDto.getTypeSaisie())) {
								if (isHeureFinLendemain(dto.getSaisies().get(iJour).getDate(), heureDebut, minuteDebut,
										heureFin, minuteFin)) {
									dtoPrime.setSaisieJ1("Attention fin de saisie j+1");
								} else {
									dtoPrime.setSaisieJ1(null);
								}

								BindUtils.postNotifyChange(null, null, dtoPrime, "saisieJ1");
								break;
							}
						}
						iJour++;
					}
				}
			}
		}
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
	}

	@Command
	public void textChangedAbs(@BindingParam("ref") AbsenceDtoKiosque dtoAbs,
			@BindingParam("heureDebut") String heureDebut, @BindingParam("minuteDebut") String minuteDebut,
			@BindingParam("heureFin") String heureFin, @BindingParam("minuteFin") String minuteFin) {
		setHasTextChanged(true);
		FichePointageDtoKiosque dto = getFicheCourante();
		if (heureDebut != null && minuteDebut != null && heureFin != null && minuteFin != null) {
			// les absences
			if (null != getSaisiePointageForm().getMapAllAbsence()) {
				Map<String, List<AbsenceDtoKiosque>> mapAllAbsence = getSaisiePointageForm().getMapAllAbsence();
				// 1ere saisie (1ere ligne)
				int iJour = 0;
				if (0 < mapAllAbsence.size()) {
					for (AbsenceDtoKiosque absenceDto : mapAllAbsence.get("0")) {
						if (null != absenceDto.getIdRefEtat()) {
							if (isHeureFinLendemain(dto.getSaisies().get(iJour).getDate(), heureDebut, minuteDebut,
									heureFin, minuteFin)) {
								dtoAbs.setSaisieJ1("Attention fin de saisie j+1");
							} else {
								dtoAbs.setSaisieJ1(null);
							}

							BindUtils.postNotifyChange(null, null, dtoAbs, "saisieJ1");
							break;
						}
						iJour++;
					}
				}
				// 2e saisie (2e ligne)
				if (1 < mapAllAbsence.size()) {
					iJour = 0;
					for (AbsenceDtoKiosque absenceDto : mapAllAbsence.get("1")) {
						if (null != absenceDto.getIdRefEtat()) {
							if (isHeureFinLendemain(dto.getSaisies().get(iJour).getDate(), heureDebut, minuteDebut,
									heureFin, minuteFin)) {
								dtoAbs.setSaisieJ1("Attention fin de saisie j+1");
							} else {
								dtoAbs.setSaisieJ1(null);
							}

							BindUtils.postNotifyChange(null, null, dtoAbs, "saisieJ1");
							break;
						}
						iJour++;
					}
				}
			}
		}
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
	}

	@Command
	public void textChangedHSup(@BindingParam("ref") HeureSupDtoKiosque dtoHsup,
			@BindingParam("heureDebut") String heureDebut, @BindingParam("minuteDebut") String minuteDebut,
			@BindingParam("heureFin") String heureFin, @BindingParam("minuteFin") String minuteFin) {
		setHasTextChanged(true);
		FichePointageDtoKiosque dto = getFicheCourante();
		if (heureDebut != null && minuteDebut != null && heureFin != null && minuteFin != null) {
			// les heures supp
			if (null != getSaisiePointageForm().getMapAllHSup()) {
				Map<String, List<HeureSupDtoKiosque>> mapAllHSup = getSaisiePointageForm().getMapAllHSup();
				// 1ere saisie (1ere ligne)
				int iJour = 0;
				if (0 < mapAllHSup.size()) {
					for (HeureSupDtoKiosque hSupDto : mapAllHSup.get("0")) {
						if (null != hSupDto.getIdRefEtat()) {
							if (isHeureFinLendemain(dto.getSaisies().get(iJour).getDate(), heureDebut, minuteDebut,
									heureFin, minuteFin)) {
								dtoHsup.setSaisieJ1("Attention fin de saisie j+1");
							} else {
								dtoHsup.setSaisieJ1(null);
							}

							BindUtils.postNotifyChange(null, null, dtoHsup, "saisieJ1");
							break;
						}
						iJour++;
					}
				}
				// 2e saisie (2e ligne)
				if (1 < mapAllHSup.size()) {
					iJour = 0;
					for (HeureSupDtoKiosque hSupDto : mapAllHSup.get("1")) {
						if (null != hSupDto.getIdRefEtat()) {
							if (isHeureFinLendemain(dto.getSaisies().get(iJour).getDate(), heureDebut, minuteDebut,
									heureFin, minuteFin)) {
								dtoHsup.setSaisieJ1("Attention fin de saisie j+1");
							} else {
								dtoHsup.setSaisieJ1(null);
							}

							BindUtils.postNotifyChange(null, null, dtoHsup, "saisieJ1");
							break;
						}
						iJour++;
					}
				}
			}
		}
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
	}

	private boolean isHeureFinLendemain(Date dateJour, String heureDebut, String minuteDebut, String heureFin,
			String minuteFin) {
		if (dateJour != null && heureDebut != null && minuteDebut != null && heureFin != null && minuteFin != null) {
			Calendar calFin = Calendar.getInstance();
			calFin.setTime(dateJour);
			calFin.add(Calendar.HOUR_OF_DAY, Integer.valueOf(heureFin));
			calFin.add(Calendar.MINUTE, Integer.valueOf(minuteFin));
			Calendar calDebut = Calendar.getInstance();
			calDebut.setTime(dateJour);
			calDebut.add(Calendar.HOUR_OF_DAY, Integer.valueOf(heureDebut));
			calDebut.add(Calendar.MINUTE, Integer.valueOf(minuteDebut));
			// si date de debut sup a la date de fin alors alerte
			if (calFin.before(calDebut)) {
				return true;
			}
		}
		return false;

	}

	@Command
	@NotifyChange({ "*" })
	public void enregistreFiche() throws ParseException {
		setFicheCourante(transformFromSaisiePointageFormToFichePointageDto(getSaisiePointageForm()));
		ReturnMessageDto result = isFormValid(getFicheCourante());
		if (result.getErrors().size() > 0) {

			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
			for (String error : result.getErrors()) {
				ValidationMessage vm = new ValidationMessage(error);
				listErreur.add(vm);
			}
			map.put("errors", listErreur);
			map.put("infos", listInfo);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
		} else {

			result = ptgWsConsumer.setFichePointageSaisie(currentUser.getAgent().getIdAgent(), getFicheCourante());

			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();

			if (result.getErrors().size() == 0) {
				result.getInfos().add("La saisie a été enregistrée correctement.");
				setHasTextChanged(false);
			}
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
			
			//#15508
			chargeFichePointage();
		}
	}

	private ReturnMessageDto isFormValid(FichePointageDtoKiosque ficheCourante) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ReturnMessageDto result = new ReturnMessageDto();
		for (JourPointageDtoKiosque dtoJour : ficheCourante.getSaisies()) {
			// absence
			for (AbsenceDtoKiosque absDto : dtoJour.getAbsences()) {
				if (absDto.getMotif() == null || absDto.getMotif().equals("")) {
					result.getErrors()
							.add(String.format("Le motif de l'absence du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (absDto.getHeureDebut() == null || absDto.getMinuteDebut() == null) {
					result.getErrors().add(
							String.format("L'heure de début de l'absence du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (absDto.getHeureFin() == null || absDto.getMinuteFin() == null) {
					result.getErrors().add(
							String.format("L'heure de fin de l'absence du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (absDto.getIdRefTypeAbsence() == null) {
					result.getErrors().add(
							String.format("Le type d'absence de l'absence du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
			}
			// heure sup
			for (HeureSupDtoKiosque hsupDto : dtoJour.getHeuresSup()) {
				if (hsupDto.getIdMotifHsup() == null) {
					result.getErrors().add(
							String.format("Le motif de l'heure supplémentaire du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (hsupDto.getHeureDebut() == null) {
					result.getErrors().add(
							String.format("L'heure de début de l'heure supplémentaire du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (hsupDto.getHeureFin() == null) {
					result.getErrors().add(
							String.format("L'heure de fin de l'heure supplémentaire du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
			}
			// primes
			for (PrimeDtoKiosque primeDto : dtoJour.getPrimes()) {
				if (periodeHeure(primeDto.getTypeSaisie())) {
					if (primeDto.getHeureDebut() == null) {
						result.getErrors().add(
								String.format("L'heure de début de la prime %s du %s est obligatoire.",
										primeDto.getTitre(), sdf.format(dtoJour.getDate())));
					}
					if (primeDto.getHeureFin() == null) {
						result.getErrors().add(
								String.format("L'heure de fin de la prime %s du %s est obligatoire.",
										primeDto.getTitre(), sdf.format(dtoJour.getDate())));
					}
				} else if (nbHeures(primeDto.getTypeSaisie())) {
					if (primeDto.getQuantite() == null || primeDto.getQuantite() == 0) {
						result.getErrors().add(
								String.format("Le nombre d'heures de la prime %s du %s est obligatoire.",
										primeDto.getTitre(), sdf.format(dtoJour.getDate())));
					}
				} else if (nbIndemnites(primeDto.getTypeSaisie())) {
					if (primeDto.getQuantite() == null || primeDto.getQuantite() == 0) {
						result.getErrors().add(
								String.format("Le nombre d'indemnité de la prime %s du %s est obligatoire.",
										primeDto.getTitre(), sdf.format(dtoJour.getDate())));
					}
				}
			}
		}
		return result;
	}

	@Command
	public void ajouterHSup(@BindingParam("ref") HeureSupDtoKiosque hsup) {
		hsup.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
		setHasTextChanged(true);
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		BindUtils.postNotifyChange(null, null, hsup, "*");
		BindUtils.postNotifyChange(null, null, hsup, ".");
	}

	@Command
	public void deleteHSup(@BindingParam("ref") HeureSupDtoKiosque hsup) {
		hsup.setIdRefEtat(null);
		hsup.setCommentaire(null);
		hsup.setIdMotifHsup(null);
		hsup.setHeureDebut(null);
		hsup.setMinuteDebut(null);
		hsup.setHeureFin(null);
		hsup.setMinuteFin(null);
		hsup.setRappelService(false);
		hsup.setIdPointage(null);

		hsup = setHSupARecupererForDPM(hsup);
		setHasTextChanged(true);
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		BindUtils.postNotifyChange(null, null, hsup, "*");
		BindUtils.postNotifyChange(null, null, hsup, ".");
	}

	@Command
	public void ajouterAbsence(@BindingParam("ref") AbsenceDtoKiosque absence) {
		absence.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
		setHasTextChanged(true);
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		BindUtils.postNotifyChange(null, null, absence, "*");
		BindUtils.postNotifyChange(null, null, absence, ".");
	}

	@Command
	public void deleteAbsence(@BindingParam("ref") AbsenceDtoKiosque absence) {
		absence.setIdRefEtat(null);
		absence.setCommentaire(null);
		absence.setMotif(null);
		absence.setHeureDebut(null);
		absence.setMinuteDebut(null);
		absence.setHeureFin(null);
		absence.setMinuteFin(null);
		absence.setIdPointage(null);
		absence.setIdRefTypeAbsence(null);
		setHasTextChanged(true);
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		BindUtils.postNotifyChange(null, null, absence, "*");
		BindUtils.postNotifyChange(null, null, absence, ".");
	}

	@Command
	public void ajouterPrime(@BindingParam("ref") PrimeDtoKiosque prime) {
		prime.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
		if (caseACocher(prime.getTypeSaisie())) {
			prime.setQuantite(1);
		}
		setHasTextChanged(true);

		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		BindUtils.postNotifyChange(null, null, prime, "*");
		BindUtils.postNotifyChange(null, null, prime, ".");
	}

	@Command
	public void deletePrime(@BindingParam("ref") PrimeDtoKiosque prime) {
		prime.setIdRefEtat(null);
		prime.setCommentaire(null);
		prime.setMotif(null);
		prime.setHeureDebut(null);
		prime.setMinuteDebut(null);
		prime.setHeureFin(null);
		prime.setMinuteFin(null);
		prime.setIdPointage(null);
		prime.setQuantite(null);
		setHasTextChanged(true);
		BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		BindUtils.postNotifyChange(null, null, prime, "*");
		BindUtils.postNotifyChange(null, null, prime, ".");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "*" })
	public void chargeFiche() throws ParseException {
		if (isHasTextChanged()) {
			// on ouvre une popup de confirmation
			Messagebox
					.show("Vous avez effectué des modifications sur les pointages sans les enregistrer, si vous continuez, vous allez perdre ces modifications. Voulez-vous continuer ?",
							"Confirmation", Messagebox.CANCEL | Messagebox.OK, "", new EventListener() {
								@Override
								public void onEvent(Event evt) throws InterruptedException, ParseException {
									if (evt.getName().equals("onOK")) {
										setHasTextChanged(false);

										chargeFichePointage();
										BindUtils.postNotifyChange(null, null, SaisieHebdomadaireViewModel.this, "*");
									}
								}
							});
		} else {
			setHasTextChanged(false);

			chargeFichePointage();
		}
	}
	
	// #15508
	private void chargeFichePointage() {
		if (null != getDateLundi() && null != getAgentFiltre() && null != getAgentFiltre().getIdAgent()) {
			FichePointageDtoKiosque result = ptgWsConsumer.getFichePointageSaisie(currentUser.getAgent()
					.getIdAgent(), getLundi(getDateLundi()), getAgentFiltre().getIdAgent());
			setFicheCourante(result);

			// minutes et heures
			TimePicker timePicker = new TimePicker();
			setListeMinutes(timePicker.getListeMinutesPointage());
			setListeHeures(timePicker.getListeHeuresPointage());

			setSaisiePointageForm(transformFromFichePointageDtoToSaisiePointageForm(getFicheCourante()));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "*" })
	public void afficheSemaine() throws ParseException {
		if (isHasTextChanged()) {
			// on ouvre une popup de confirmation
			Messagebox
					.show("Vous avez effectué des modifications sur les pointages sans les enregistrer, si vous continuez, vous allez perdre ces modifications. Voulez-vous continuer ?",
							"Confirmation", Messagebox.CANCEL | Messagebox.OK, "", new EventListener() {
								@Override
								public void onEvent(Event evt) throws InterruptedException, ParseException {
									if (evt.getName().equals("onOK")) {
										if (getDateLundi() != null) {
											SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
											Calendar c = Calendar.getInstance();
											c.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
											c.setTime(getDateLundi());
											String numSemaine = String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
											c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
											String lundi = sdf.format(c.getTime());
											c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
											String dimanche = sdf.format(c.getTime());

											setDateFiltre("Semaine " + numSemaine + " du " + lundi + " au " + dimanche);
										}
										setHasTextChanged(false);
										chargeFiche();
										BindUtils.postNotifyChange(null, null, SaisieHebdomadaireViewModel.this, "*");
									}
								}
							});
		} else {
			if (getDateLundi() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar c = Calendar.getInstance();
				c.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
				c.setTime(getDateLundi());
				String numSemaine = String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				String lundi = sdf.format(c.getTime());
				c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				String dimanche = sdf.format(c.getTime());

				setDateFiltre("Semaine " + numSemaine + " du " + lundi + " au " + dimanche);
			}
			setHasTextChanged(false);
			chargeFiche();
			BindUtils.postNotifyChange(null, null, SaisieHebdomadaireViewModel.this, "*");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void afficheListeAgent() throws ParseException {
		if (isHasTextChanged()) {
			// on ouvre une popup de confirmation
			Messagebox
					.show("Vous avez effectué des modifications sur les pointages sans les enregistrer, si vous continuez, vous allez perdre ces modifications. Voulez-vous continuer ?",
							"Confirmation", Messagebox.CANCEL | Messagebox.OK, "", new EventListener() {
								@Override
								public void onEvent(Event evt) throws InterruptedException, ParseException {
									if (evt.getName().equals("onOK")) {
										setHasTextChanged(false);
										setAgentFiltre(null);
										// on charge les agents pour les filtres
										List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(currentUser
												.getAgent().getIdAgent(), getServiceFiltre().getCodeService());
										setListeAgentsFiltre(filtreAgent);
										if (getAgentFiltre() == null) {
											setSaisiePointageForm(null);
										}
										if (getListeAgentsFiltre() != null && getListeAgentsFiltre().size() == 1) {
											setAgentFiltre(getListeAgentsFiltre().get(0));
											chargeFiche();
										}
										BindUtils.postNotifyChange(null, null, SaisieHebdomadaireViewModel.this, "*");
									}
								}
							});
		} else {
			setHasTextChanged(false);
			setAgentFiltre(null);
			// on charge les agents pour les filtres
			List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(currentUser.getAgent().getIdAgent(),
					getServiceFiltre().getCodeService());
			setListeAgentsFiltre(filtreAgent);
			if (getAgentFiltre() == null) {
				setSaisiePointageForm(null);
			}
			if (getListeAgentsFiltre() != null && getListeAgentsFiltre().size() == 1) {
				setAgentFiltre(getListeAgentsFiltre().get(0));
				chargeFiche();
			}
			BindUtils.postNotifyChange(null, null, SaisieHebdomadaireViewModel.this, "*");
		}
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	@Command
	@NotifyChange({ "ficheCourante" })
	public void checkAccorde(@BindingParam("prime") PrimeDtoKiosque prime, @BindingParam("checkbox") Checkbox checkbox) {
		prime.setQuantite(checkbox.isChecked() ? 1 : null);
	}

	public String etatToString(Integer idRefEtat) {
		return EtatPointageEnum.getEtatPointageEnum(idRefEtat).getLibEtat();
	}

	public boolean periodeHeure(String typeSaisie) {
		return typeSaisie.equals("PERIODE_HEURES");
	}

	public boolean caseACocher(String typeSaisie) {
		return typeSaisie.equals("CASE_A_COCHER");
	}

	public boolean nbHeures(String typeSaisie) {
		return typeSaisie.equals("NB_HEURES");
	}

	public boolean nbIndemnites(String typeSaisie) {
		return typeSaisie.equals("NB_INDEMNITES");
	}

	public boolean checkCoche(Integer quantite) {
		return quantite != null && quantite != 0;
	}

	public boolean isCheckCoche(Integer quantite) {
		return quantite != null && quantite != 0;
	}

	public String labelCoche(Integer quantite) {
		return quantite == null ? "Non" : "Oui";
	}

	public boolean estAgentDPM() {
		return getFicheCourante().isDPM();
	}

	public boolean estINASuperieur315() {
		return getFicheCourante().isINASuperieur315();
	}

	public FichePointageDtoKiosque getFicheCourante() {
		return ficheCourante;
	}

	public void setFicheCourante(FichePointageDtoKiosque ficheCourante) {
		this.ficheCourante = ficheCourante;
	}

	private Date getLundi(Date dateLundi) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("Pacific/Noumea"));
		c.setTime(dateLundi);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c.getTime();
	}

	public List<ServiceDto> getListeServicesFiltre() {
		return listeServicesFiltre;
	}

	public void setListeServicesFiltre(List<ServiceDto> listeServicesFiltre) {
		if(null != listeServicesFiltre) {
			Collections.sort(listeServicesFiltre);
		}
		this.listeServicesFiltre = listeServicesFiltre;
	}

	public ServiceDto getServiceFiltre() {
		return serviceFiltre;
	}

	public void setServiceFiltre(ServiceDto serviceFiltre) {
		this.serviceFiltre = serviceFiltre;
	}

	public String getDateFiltre() {
		return dateFiltre;
	}

	public void setDateFiltre(String dateFiltre) {
		this.dateFiltre = dateFiltre;
	}

	public Date getDateLundi() {
		return dateLundi;
	}

	public void setDateLundi(Date dateLundi) {
		this.dateLundi = dateLundi;
	}

	public List<AgentDto> getListeAgentsFiltre() {
		return listeAgentsFiltre;
	}

	public void setListeAgentsFiltre(List<AgentDto> listeAgentsFiltre) {
		if(null != listeAgentsFiltre) {
			Collections.sort(listeAgentsFiltre);
		}
		this.listeAgentsFiltre = listeAgentsFiltre;
	}

	public AgentDto getAgentFiltre() {
		return agentFiltre;
	}

	public void setAgentFiltre(AgentDto agentFiltre) {
		this.agentFiltre = agentFiltre;
	}

	public PrimeDtoKiosque getJour(Integer i) {
		Listitem p = new Listitem();
		p.setValue(getFicheCourante().getSaisies().get(0).getPrimes().get(0));
		getFicheCourante().getSaisies().get(0).getPrimes().get(0).setMotif("motif nono");
		return getFicheCourante().getSaisies().get(0).getPrimes().get(0);

	}

	public SaisiePointageForm getSaisiePointageForm() {
		return saisiePointageForm;
	}

	public void setSaisiePointageForm(SaisiePointageForm saisiePointageForm) {
		this.saisiePointageForm = saisiePointageForm;
	}

	private SaisiePointageForm transformFromFichePointageDtoToSaisiePointageForm(FichePointageDtoKiosque dto) {

		SaisiePointageForm form = new SaisiePointageForm();

		Map<String, List<PrimeDtoKiosque>> mapAllPrime = new HashMap<String, List<PrimeDtoKiosque>>();
		Map<String, List<AbsenceDtoKiosque>> mapAllAbsence = new HashMap<String, List<AbsenceDtoKiosque>>();
		Map<String, List<HeureSupDtoKiosque>> mapAllHSup = new HashMap<String, List<HeureSupDtoKiosque>>();

		if (null != dto.getSaisies()) {

			int nbrLigneAbsence = 1;
			int nbrLigneHSup = 1;

			for (JourPointageDtoKiosque jour : dto.getSaisies()) {
				// on determine une ou deux lignes pour les absences
				if (null != jour.getAbsences() && 2 == jour.getAbsences().size()) {
					nbrLigneAbsence = 2;
				}

				if (null != jour.getHeuresSup() && 2 == jour.getHeuresSup().size()) {
					nbrLigneHSup = 2;
				}
			}
			for (JourPointageDtoKiosque jour : dto.getSaisies()) {

				if (!form.getListDateJour().contains(jour.getDate())) {
					form.getListDateJour().add(jour.getDate());
				}

				for (PrimeDtoKiosque prime : jour.getPrimes()) {
					if (mapAllPrime.containsKey(prime.getNumRubrique().toString())) {
						
						if(nbHeures(prime.getTypeSaisie())
								&& null != prime.getQuantite()
								&& 0 != prime.getQuantite()) {
							String heureDebut = "";
							String minuteDebut = "";
							
							DecimalFormat df = new DecimalFormat( "00" );
							heureDebut = df.format(prime.getQuantite()/60);
							minuteDebut = df.format(prime.getQuantite()%60);
							prime.setHeureDebut(heureDebut);
							prime.setMinuteDebut(minuteDebut);
						}
						
						mapAllPrime.get(prime.getNumRubrique().toString()).add(prime);
					} else {
						List<PrimeDtoKiosque> newListPrime = new ArrayList<PrimeDtoKiosque>();
						newListPrime.add(prime);
						mapAllPrime.put(prime.getNumRubrique().toString(), newListPrime);
					}
				}

				for (int i = 0; i < nbrLigneAbsence; i++) {
					if (i < jour.getAbsences().size()) {
						AbsenceDtoKiosque absence = jour.getAbsences().get(i);
						if (mapAllAbsence.containsKey(new Integer(i).toString())) {
							mapAllAbsence.get(new Integer(i).toString()).add(absence);
						} else {
							List<AbsenceDtoKiosque> newListAbsence = new ArrayList<AbsenceDtoKiosque>();
							newListAbsence.add(absence);
							mapAllAbsence.put(new Integer(i).toString(), newListAbsence);
						}
					} else {
						if (mapAllAbsence.containsKey(new Integer(i).toString())) {
							mapAllAbsence.get(new Integer(i).toString()).add(new AbsenceDtoKiosque());
						} else {
							List<AbsenceDtoKiosque> newListAbsence = new ArrayList<AbsenceDtoKiosque>();
							newListAbsence.add(new AbsenceDtoKiosque());
							mapAllAbsence.put(new Integer(i).toString(), newListAbsence);
						}
					}
				}

				for (int i = 0; i < nbrLigneHSup; i++) {
					if (i < jour.getHeuresSup().size()) {
						HeureSupDtoKiosque hsup = jour.getHeuresSup().get(i);
						hsup = setHSupARecupererForDPM(hsup);
						if (mapAllHSup.containsKey(new Integer(i).toString())) {
							mapAllHSup.get(new Integer(i).toString()).add(hsup);
						} else {
							List<HeureSupDtoKiosque> newListHSup = new ArrayList<HeureSupDtoKiosque>();
							newListHSup.add(hsup);
							mapAllHSup.put(new Integer(i).toString(), newListHSup);
						}
					} else {
						HeureSupDtoKiosque newHsupDtp = setHSupARecupererForDPM(new HeureSupDtoKiosque());
						if (mapAllHSup.containsKey(new Integer(i).toString())) {
							mapAllHSup.get(new Integer(i).toString()).add(newHsupDtp);
						} else {
							List<HeureSupDtoKiosque> newListHsup = new ArrayList<HeureSupDtoKiosque>();
							newListHsup.add(newHsupDtp);
							mapAllHSup.put(new Integer(i).toString(), newListHsup);
						}
					}
				}
			}

			form.setMapAllAbsence(mapAllAbsence);
			form.setMapAllHSup(mapAllHSup);
			form.setMapAllPrime(mapAllPrime);
		}

		return form;
	}

	public boolean estCheckboxHSupARecupererDisabled(HeureSupDtoKiosque hSupDto) {
		if (null != hSupDto.getIdRefEtat() && !estAgentDPM() && !estINASuperieur315()) {
			return false;
		}
		return true;
	}

	private Date calculDateEtHeureSaisie(Date dateJour, String heureSaisie, String minuteSaisie, String saisieJ1) {
		if (heureSaisie != null && minuteSaisie != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateJour);
			if (saisieJ1 != null) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			cal.add(Calendar.HOUR_OF_DAY, new Integer(heureSaisie));
			cal.add(Calendar.MINUTE, new Integer(minuteSaisie));
			return cal.getTime();
		}
		return null;
	}

	private HeureSupDtoKiosque setHSupARecupererForDPM(HeureSupDtoKiosque hSupDto) {
		if (estAgentDPM() || estINASuperieur315())
			hSupDto.setRecuperee(true);

		return hSupDto;
	}

	private FichePointageDtoKiosque transformFromSaisiePointageFormToFichePointageDto(SaisiePointageForm form) {

		FichePointageDtoKiosque dto = getFicheCourante();

		// les absences
		if (null != form.getMapAllAbsence()) {
			Map<String, List<AbsenceDtoKiosque>> mapAllAbsence = form.getMapAllAbsence();
			// 1ere saisie (1ere ligne)
			int iJour = 0;
			if (0 < mapAllAbsence.size()) {
				for (AbsenceDtoKiosque absenceDto : mapAllAbsence.get("0")) {
					dto.getSaisies().get(iJour).getAbsences().clear();
					if (null != absenceDto.getIdRefEtat()) {
						absenceDto.setHeureDebutDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureDebut(), absenceDto.getMinuteDebut(), null));
						absenceDto.setHeureFinDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureFin(), absenceDto.getMinuteFin(), absenceDto.getSaisieJ1()));

						dto.getSaisies().get(iJour).getAbsences().add(absenceDto);
					}
					iJour++;
				}
			}
			// 2e saisie (2e ligne)
			if (1 < mapAllAbsence.size()) {
				iJour = 0;
				for (AbsenceDtoKiosque absenceDto : mapAllAbsence.get("1")) {
					if (null != absenceDto.getIdRefEtat()) {
						absenceDto.setHeureDebutDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureDebut(), absenceDto.getMinuteDebut(), null));
						absenceDto.setHeureFinDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureFin(), absenceDto.getMinuteFin(), absenceDto.getSaisieJ1()));

						dto.getSaisies().get(iJour).getAbsences().add(absenceDto);
					}
					iJour++;
				}
			}
		}

		// les heures supp
		if (null != form.getMapAllHSup()) {
			Map<String, List<HeureSupDtoKiosque>> mapAllHSup = form.getMapAllHSup();
			// 1ere saisie (1ere ligne)
			int iJour = 0;
			if (0 < mapAllHSup.size()) {
				for (HeureSupDtoKiosque hSupDto : mapAllHSup.get("0")) {
					dto.getSaisies().get(iJour).getHeuresSup().clear();
					if (null != hSupDto.getIdRefEtat()) {
						hSupDto.setHeureDebutDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureDebut(), hSupDto.getMinuteDebut(), null));
						hSupDto.setHeureFinDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureFin(), hSupDto.getMinuteFin(), hSupDto.getSaisieJ1()));

						dto.getSaisies().get(iJour).getHeuresSup().add(hSupDto);
					}
					iJour++;
				}
			}
			// 2e saisie (2e ligne)
			if (1 < mapAllHSup.size()) {
				iJour = 0;
				for (HeureSupDtoKiosque hSupDto : mapAllHSup.get("1")) {
					if (null != hSupDto.getIdRefEtat()) {
						hSupDto.setHeureDebutDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureDebut(), hSupDto.getMinuteDebut(), null));
						hSupDto.setHeureFinDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureFin(), hSupDto.getMinuteFin(), hSupDto.getSaisieJ1()));

						dto.getSaisies().get(iJour).getHeuresSup().add(hSupDto);
					}
					iJour++;
				}
			}
		}

		// les primes
		// on nettoie l'existant
		for (JourPointageDtoKiosque jourPtg : dto.getSaisies()) {
			jourPtg.getPrimes().clear();
		}
		// on mappe
		if (null != form.getMapAllPrime()) {
			Map<String, List<PrimeDtoKiosque>> mapAllPrime = form.getMapAllPrime();

			for (String key : mapAllPrime.keySet()) {
				List<PrimeDtoKiosque> listPrimes = mapAllPrime.get(key);
				int iJour = 0;
				for (PrimeDtoKiosque primeDto : listPrimes) {
					if (null != primeDto.getIdRefEtat()) {

						if (periodeHeure(primeDto.getTypeSaisie())) {
							primeDto.setHeureDebutDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
									primeDto.getHeureDebut(), primeDto.getMinuteDebut(), null));
							primeDto.setHeureFinDate(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
									primeDto.getHeureFin(), primeDto.getMinuteFin(), primeDto.getSaisieJ1()));
						}
						if(nbHeures(primeDto.getTypeSaisie())
								&& null != primeDto.getHeureDebut()
								&& null != primeDto.getMinuteDebut()) {
							Integer quantite = 0;
							quantite = new Integer(primeDto.getHeureDebut()) * 60 +  new Integer(primeDto.getMinuteDebut());
							primeDto.setQuantite(quantite);
						}

						dto.getSaisies().get(iJour).getPrimes().add(primeDto);
					}
					iJour++;
				}
			}
		}

		return dto;
	}

	@Command
	@NotifyChange({ "saisiePointageForm" })
	public void ajouterLigneHSup() {
		if (null != getSaisiePointageForm().getMapAllHSup() && 2 > getSaisiePointageForm().getMapAllHSup().size()) {

			List<HeureSupDtoKiosque> newListHsup = new ArrayList<HeureSupDtoKiosque>();
			for (int i = 0; i < 7; i++) {
				newListHsup.add(new HeureSupDtoKiosque());
			}

			getSaisiePointageForm().getMapAllHSup().put(new Integer(1).toString(), newListHsup);
		}
	}

	@Command
	@NotifyChange({ "saisiePointageForm" })
	public void ajouterLigneAbsence() {
		if (null != getSaisiePointageForm().getMapAllAbsence() && 2 > getSaisiePointageForm().getMapAllAbsence().size()) {

			List<AbsenceDtoKiosque> newListAbsence = new ArrayList<AbsenceDtoKiosque>();
			for (int i = 0; i < 7; i++) {
				newListAbsence.add(new AbsenceDtoKiosque());
			}

			getSaisiePointageForm().getMapAllAbsence().put(new Integer(1).toString(), newListAbsence);
		}
	}

	public ListModel<RefTypeAbsenceDto> getListeTypeAbsence() {
		return listeTypeAbsence;
	}

	public void setListeTypeAbsence(ListModel<RefTypeAbsenceDto> listeTypeAbsence) {
		this.listeTypeAbsence = listeTypeAbsence;
	}

	public boolean isHasTextChanged() {
		return hasTextChanged;
	}

	public void setHasTextChanged(boolean hasTextChanged) {
		this.hasTextChanged = hasTextChanged;
	}

	public boolean isAfficherDupliquerPrimeGauche(PrimeDtoKiosque prime) {
		if (prime.getIdRefEtat() == null) {
			return false;
		}
		if (getSaisiePointageForm() != null) {
			for (int i = 0; i < getFicheCourante().getSaisies().size(); i++) {
				int jour = 0;
				for (PrimeDtoKiosque dto : getFicheCourante().getSaisies().get(jour).getPrimes()) {
					if (getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString()) != null) {
						int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString())
								.indexOf(prime);
						if (indexPart2 == 0) {
							return false;
						}
					}
					jour++;
				}
			}
		}

		return true;
	}

	public boolean isAfficherDupliquerPrimeDroit(PrimeDtoKiosque prime) {
		if (prime.getIdRefEtat() == null) {
			return false;
		}

		for (int i = 0; i < getSaisiePointageForm().getMapAllPrime().size(); i++) {
			int jour = 0;
			for (PrimeDtoKiosque dto : getFicheCourante().getSaisies().get(jour).getPrimes()) {
				if (getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString()) != null) {
					int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString())
							.indexOf(prime);
					if (indexPart2 == (getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString())
							.size() - 1)) {
						return false;
					}
				}
				jour++;
			}
		}
		return true;
	}

	public boolean isAfficherDupliquerAbsenceGauche(AbsenceDtoKiosque absence) {
		if (absence.getIdRefEtat() == null) {
			return false;
		}
		if (getSaisiePointageForm() != null) {
			for (int i = 0; i < getSaisiePointageForm().getMapAllAbsence().size(); i++) {
				if (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)) != null) {
					int indexPart2 = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).indexOf(absence);
					if (indexPart2 == 0) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public boolean isAfficherDupliquerAbsenceDroit(AbsenceDtoKiosque absence) {
		if (absence.getIdRefEtat() == null) {
			return false;
		}

		if (getSaisiePointageForm() != null) {
			for (int i = 0; i < getSaisiePointageForm().getMapAllAbsence().size(); i++) {
				if (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)) != null) {
					int indexPart2 = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).indexOf(absence);
					if (indexPart2 == (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).size() - 1)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isAfficherDupliquerHSupGauche(HeureSupDtoKiosque hsup) {
		if (hsup.getIdRefEtat() == null) {
			return false;
		}
		if (getSaisiePointageForm() != null) {
			for (int i = 0; i < getSaisiePointageForm().getMapAllHSup().size(); i++) {
				if (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)) != null) {
					int indexPart2 = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).indexOf(hsup);
					if (indexPart2 == 0) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public boolean isAfficherDupliquerHSupDroit(HeureSupDtoKiosque hsup) {
		if (hsup.getIdRefEtat() == null) {
			return false;
		}

		if (getSaisiePointageForm() != null) {
			for (int i = 0; i < getSaisiePointageForm().getMapAllHSup().size(); i++) {
				if (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)) != null) {
					int indexPart2 = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).indexOf(hsup);
					if (indexPart2 == (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).size() - 1)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Command
	public void copieDroiteAbsence(@BindingParam("ref") AbsenceDtoKiosque absence) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllAbsence().size(); i++) {
			if (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).indexOf(absence);
				if (indexPart2 != -1) {
					AbsenceDtoKiosque absSuiv = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i))
							.get(indexPart2 + 1);
					ajouterAbsence(absSuiv);
					// on copie les données
					absSuiv.setCommentaire(absence.getCommentaire());
					absSuiv.setMotif(absence.getMotif());
					absSuiv.setHeureDebut(absence.getHeureDebut());
					absSuiv.setMinuteDebut(absence.getMinuteDebut());
					absSuiv.setHeureFin(absence.getHeureFin());
					absSuiv.setMinuteFin(absence.getMinuteFin());
					absSuiv.setIdRefTypeAbsence(absence.getIdRefTypeAbsence());
					absSuiv.setSaisieJ1(absence.getSaisieJ1());
					setHasTextChanged(true);
					BindUtils.postNotifyChange(null, null, absence, "*");
					BindUtils.postNotifyChange(null, null, absence, ".");
					BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
				}
			}
		}
	}

	@Command
	public void copieGaucheAbsence(@BindingParam("ref") AbsenceDtoKiosque absence) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllAbsence().size(); i++) {
			if (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).indexOf(absence);
				if (indexPart2 != -1) {
					AbsenceDtoKiosque absPrec = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i))
							.get(indexPart2 - 1);
					ajouterAbsence(absPrec);
					// on copie les données
					absPrec.setCommentaire(absence.getCommentaire());
					absPrec.setMotif(absence.getMotif());
					absPrec.setHeureDebut(absence.getHeureDebut());
					absPrec.setMinuteDebut(absence.getMinuteDebut());
					absPrec.setHeureFin(absence.getHeureFin());
					absPrec.setMinuteFin(absence.getMinuteFin());
					absPrec.setIdRefTypeAbsence(absence.getIdRefTypeAbsence());
					absPrec.setSaisieJ1(absence.getSaisieJ1());
					setHasTextChanged(true);
					BindUtils.postNotifyChange(null, null, absence, "*");
					BindUtils.postNotifyChange(null, null, absence, ".");
					BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
				}
			}
		}
	}

	@Command
	public void copieDroiteHSup(@BindingParam("ref") HeureSupDtoKiosque hsup) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllHSup().size(); i++) {
			if (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).indexOf(hsup);
				if (indexPart2 != -1) {
					HeureSupDtoKiosque hsupSuiv = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i))
							.get(indexPart2 + 1);
					ajouterHSup(hsupSuiv);
					// on copie les données
					hsupSuiv.setCommentaire(hsup.getCommentaire());
					hsupSuiv.setIdMotifHsup(hsup.getIdMotifHsup());
					hsupSuiv.setHeureDebut(hsup.getHeureDebut());
					hsupSuiv.setMinuteDebut(hsup.getMinuteDebut());
					hsupSuiv.setHeureFin(hsup.getHeureFin());
					hsupSuiv.setMinuteFin(hsup.getMinuteFin());
					hsupSuiv.setRappelService(hsup.isRappelService());
					hsupSuiv.setRecuperee(hsup.isRecuperee());
					hsupSuiv.setSaisieJ1(hsup.getSaisieJ1());
					setHasTextChanged(true);
					BindUtils.postNotifyChange(null, null, hsup, "*");
					BindUtils.postNotifyChange(null, null, hsup, ".");
					BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
				}
			}
		}
	}

	@Command
	public void copieGaucheHSup(@BindingParam("ref") HeureSupDtoKiosque hsup) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllHSup().size(); i++) {
			if (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).indexOf(hsup);
				if (indexPart2 != -1) {
					HeureSupDtoKiosque hsupPrec = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i))
							.get(indexPart2 - 1);
					ajouterHSup(hsupPrec);
					// on copie les données
					hsupPrec.setCommentaire(hsup.getCommentaire());
					hsupPrec.setIdMotifHsup(hsup.getIdMotifHsup());
					hsupPrec.setHeureDebut(hsup.getHeureDebut());
					hsupPrec.setMinuteDebut(hsup.getMinuteDebut());
					hsupPrec.setHeureFin(hsup.getHeureFin());
					hsupPrec.setMinuteFin(hsup.getMinuteFin());
					hsupPrec.setRappelService(hsup.isRappelService());
					hsupPrec.setRecuperee(hsup.isRecuperee());
					hsupPrec.setSaisieJ1(hsup.getSaisieJ1());
					setHasTextChanged(true);
					BindUtils.postNotifyChange(null, null, hsup, "*");
					BindUtils.postNotifyChange(null, null, hsup, ".");
					BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
				}
			}
		}
	}

	@Command
	public void copieDroitePrime(@BindingParam("ref") PrimeDtoKiosque prime) {
		if (getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString()) != null) {
			int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.indexOf(prime);
			PrimeDtoKiosque primeSuiv = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.get(indexPart2 + 1);
			ajouterPrime(primeSuiv);
			// on copie les données
			primeSuiv.setCommentaire(prime.getCommentaire());
			primeSuiv.setMotif(prime.getMotif());
			primeSuiv.setHeureDebut(prime.getHeureDebut());
			primeSuiv.setMinuteDebut(prime.getMinuteDebut());
			primeSuiv.setHeureFin(prime.getHeureFin());
			primeSuiv.setMinuteFin(prime.getMinuteFin());
			primeSuiv.setQuantite(prime.getQuantite());
			primeSuiv.setSaisieJ1(prime.getSaisieJ1());
			setHasTextChanged(true);
			BindUtils.postNotifyChange(null, null, prime, "*");
			BindUtils.postNotifyChange(null, null, prime, ".");
			BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		}

	}

	@Command
	public void copieGauchePrime(@BindingParam("ref") PrimeDtoKiosque prime) {
		if (getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString()) != null) {
			int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.indexOf(prime);
			PrimeDtoKiosque primePrec = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.get(indexPart2 - 1);
			ajouterPrime(primePrec);
			// on copie les données
			primePrec.setCommentaire(prime.getCommentaire());
			primePrec.setMotif(prime.getMotif());
			primePrec.setHeureDebut(prime.getHeureDebut());
			primePrec.setMinuteDebut(prime.getMinuteDebut());
			primePrec.setHeureFin(prime.getHeureFin());
			primePrec.setMinuteFin(prime.getMinuteFin());
			primePrec.setQuantite(prime.getQuantite());
			primePrec.setSaisieJ1(prime.getSaisieJ1());
			setHasTextChanged(true);
			BindUtils.postNotifyChange(null, null, prime, "*");
			BindUtils.postNotifyChange(null, null, prime, ".");
			BindUtils.postNotifyChange(null, null, this, "hasTextChanged");
		}

	}

	public ListModel<MotifHeureSupDto> getListeMotifHsup() {
		return listeMotifHsup;
	}

	public void setListeMotifHsup(ListModel<MotifHeureSupDto> listeMotifHsup) {
		this.listeMotifHsup = listeMotifHsup;
	}

	@Command
	@NotifyChange({ "*" })
	public void changeStyle(@BindingParam("contenu") Div contenu, @BindingParam("titre") Div titre) {
		if (contenu != null && titre != null) {
			if (contenu.isVisible()) {
				titre.setClass("barreTitreTypePointage");
				contenu.setVisible(false);
			} else {
				titre.setClass("barreTitreTypePointage-selected");
				contenu.setVisible(true);
			}
		}

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

	public boolean isCheckCoche() {
		return checkCoche;
	}

	public void setCheckCoche(boolean checkCoche) {
		this.checkCoche = checkCoche;
	}

}
