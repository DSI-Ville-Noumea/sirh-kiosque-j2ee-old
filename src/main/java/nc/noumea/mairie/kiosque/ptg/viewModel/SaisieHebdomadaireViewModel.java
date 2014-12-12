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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.HeureSupDto;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.MotifHeureSupDto;
import nc.noumea.mairie.kiosque.ptg.dto.PrimeDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.ServiceDto;
import nc.noumea.mairie.kiosque.ptg.form.SaisiePointageForm;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;

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

	private FichePointageDto ficheCourante;

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

	@Init
	public void initSaisieFichePointage() throws ParseException {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		setDateFiltre("Semaine ... du ... au ...");

		setListeTypeAbsence(getModelTypeAbsence());
		setListeMotifHsup(getModelMotifHsup());
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
	@NotifyChange({ "hasTextChanged" })
	public void enregistreFiche() {
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
		}
	}

	private ReturnMessageDto isFormValid(FichePointageDto ficheCourante) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ReturnMessageDto result = new ReturnMessageDto();
		for (JourPointageDto dtoJour : ficheCourante.getSaisies()) {
			// absence
			for (AbsenceDto absDto : dtoJour.getAbsences()) {
				if (absDto.getMotif() == null || absDto.getMotif().equals("")) {
					result.getErrors()
							.add(String.format("Le motif de l'absence du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (absDto.getHeureDebut() == null) {
					result.getErrors().add(
							String.format("L'heure de début de l'absence du %s est obligatoire.",
									sdf.format(dtoJour.getDate())));
				}
				if (absDto.getHeureFin() == null) {
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
			for (HeureSupDto hsupDto : dtoJour.getHeuresSup()) {
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
			for (PrimeDto primeDto : dtoJour.getPrimes()) {
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
	@NotifyChange({ "*" })
	public void ajouterHSup(@BindingParam("ref") HeureSupDto hsup) {
		hsup.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
		setHasTextChanged(true);
	}

	@Command
	@NotifyChange({ "*" })
	public void deleteHSup(@BindingParam("ref") HeureSupDto hsup) {
		hsup.setIdRefEtat(null);
		hsup.setCommentaire(null);
		hsup.setIdMotifHsup(null);
		hsup.setHeureDebut(null);
		hsup.setHeureFin(null);
		hsup.setRappelService(false);
		hsup.setIdPointage(null);

		hsup = setHSupARecupererForDPM(hsup);
	}

	@Command
	@NotifyChange({ "*" })
	public void ajouterAbsence(@BindingParam("ref") AbsenceDto absence) {
		absence.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
		setHasTextChanged(true);
	}

	@Command
	@NotifyChange({ "*" })
	public void deleteAbsence(@BindingParam("ref") AbsenceDto absence) {
		absence.setIdRefEtat(null);
		absence.setCommentaire(null);
		absence.setMotif(null);
		absence.setHeureDebut(null);
		absence.setHeureFin(null);
		absence.setIdPointage(null);
		absence.setIdRefTypeAbsence(null);
		setHasTextChanged(true);
	}

	@Command
	@NotifyChange({ "*" })
	public void ajouterPrime(@BindingParam("ref") PrimeDto prime) {
		prime.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
		if (caseACocher(prime.getTypeSaisie())) {
			prime.setQuantite(1);
		}
		setHasTextChanged(true);
	}

	@Command
	@NotifyChange({ "*" })
	public void deletePrime(@BindingParam("ref") PrimeDto prime) {
		prime.setIdRefEtat(null);
		prime.setCommentaire(null);
		prime.setMotif(null);
		prime.setHeureDebut(null);
		prime.setHeureFin(null);
		prime.setIdPointage(null);
		prime.setQuantite(null);
		setHasTextChanged(true);
	}

	@Command
	@NotifyChange({ "*" })
	public void chargeFiche() throws ParseException {
		setHasTextChanged(false);

		if (null != getDateLundi() && null != getAgentFiltre() && null != getAgentFiltre().getIdAgent()) {
			FichePointageDto result = ptgWsConsumer.getFichePointageSaisie(currentUser.getAgent().getIdAgent(),
					getLundi(getDateLundi()), getAgentFiltre().getIdAgent());
			setFicheCourante(result);

			setSaisiePointageForm(transformFromFichePointageDtoToSaisiePointageForm(getFicheCourante()));
		}
	}

	@Command
	@NotifyChange({ "*" })
	public void afficheSemaine() throws ParseException {
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
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre", "agentFiltre", "*" })
	public void afficheListeAgent() {
		setHasTextChanged(false);
		setAgentFiltre(null);
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
		if (getAgentFiltre() == null) {
			setSaisiePointageForm(null);
		}
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	@Command
	@NotifyChange({ "ficheCourante" })
	public void checkAccorde(@BindingParam("prime") PrimeDto prime, @BindingParam("checkbox") Checkbox checkbox) {
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

	public String labelCoche(Integer quantite) {
		return quantite == null ? "Non" : "Oui";
	}

	public boolean estAgentDPM() {
		return getFicheCourante().isDPM();
	}

	public FichePointageDto getFicheCourante() {
		return ficheCourante;
	}

	public void setFicheCourante(FichePointageDto ficheCourante) {
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
		this.listeAgentsFiltre = listeAgentsFiltre;
	}

	public AgentDto getAgentFiltre() {
		return agentFiltre;
	}

	public void setAgentFiltre(AgentDto agentFiltre) {
		this.agentFiltre = agentFiltre;
	}

	public PrimeDto getJour(Integer i) {
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

	private SaisiePointageForm transformFromFichePointageDtoToSaisiePointageForm(FichePointageDto dto) {

		SaisiePointageForm form = new SaisiePointageForm();

		Map<String, List<PrimeDto>> mapAllPrime = new HashMap<String, List<PrimeDto>>();
		Map<String, List<AbsenceDto>> mapAllAbsence = new HashMap<String, List<AbsenceDto>>();
		Map<String, List<HeureSupDto>> mapAllHSup = new HashMap<String, List<HeureSupDto>>();

		if (null != dto.getSaisies()) {

			int nbrLigneAbsence = 1;
			int nbrLigneHSup = 1;

			for (JourPointageDto jour : dto.getSaisies()) {
				// on determine une ou deux lignes pour les absences
				if (null != jour.getAbsences() && 2 == jour.getAbsences().size()) {
					nbrLigneAbsence = 2;
				}

				if (null != jour.getHeuresSup() && 2 == jour.getHeuresSup().size()) {
					nbrLigneHSup = 2;
				}
			}
			for (JourPointageDto jour : dto.getSaisies()) {

				if (!form.getListDateJour().contains(jour.getDate())) {
					form.getListDateJour().add(jour.getDate());
				}

				for (PrimeDto prime : jour.getPrimes()) {
					if (mapAllPrime.containsKey(prime.getNumRubrique().toString())) {
						mapAllPrime.get(prime.getNumRubrique().toString()).add(prime);
					} else {
						List<PrimeDto> newListPrime = new ArrayList<PrimeDto>();
						newListPrime.add(prime);
						mapAllPrime.put(prime.getNumRubrique().toString(), newListPrime);
					}
				}

				for (int i = 0; i < nbrLigneAbsence; i++) {
					if (i < jour.getAbsences().size()) {
						AbsenceDto absence = jour.getAbsences().get(i);
						if (mapAllAbsence.containsKey(new Integer(i).toString())) {
							mapAllAbsence.get(new Integer(i).toString()).add(absence);
						} else {
							List<AbsenceDto> newListAbsence = new ArrayList<AbsenceDto>();
							newListAbsence.add(absence);
							mapAllAbsence.put(new Integer(i).toString(), newListAbsence);
						}
					} else {
						if (mapAllAbsence.containsKey(new Integer(i).toString())) {
							mapAllAbsence.get(new Integer(i).toString()).add(new AbsenceDto());
						} else {
							List<AbsenceDto> newListAbsence = new ArrayList<AbsenceDto>();
							newListAbsence.add(new AbsenceDto());
							mapAllAbsence.put(new Integer(i).toString(), newListAbsence);
						}
					}
				}

				for (int i = 0; i < nbrLigneHSup; i++) {
					if (i < jour.getHeuresSup().size()) {
						HeureSupDto hsup = jour.getHeuresSup().get(i);
						hsup = setHSupARecupererForDPM(hsup);
						if (mapAllHSup.containsKey(new Integer(i).toString())) {
							mapAllHSup.get(new Integer(i).toString()).add(hsup);
						} else {
							List<HeureSupDto> newListHSup = new ArrayList<HeureSupDto>();
							newListHSup.add(hsup);
							mapAllHSup.put(new Integer(i).toString(), newListHSup);
						}
					} else {
						HeureSupDto newHsupDtp = setHSupARecupererForDPM(new HeureSupDto());
						if (mapAllHSup.containsKey(new Integer(i).toString())) {
							mapAllHSup.get(new Integer(i).toString()).add(newHsupDtp);
						} else {
							List<HeureSupDto> newListHsup = new ArrayList<HeureSupDto>();
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

	public boolean estCheckboxHSupARecupererDisabled(HeureSupDto hSupDto) {
		if (null != hSupDto.getIdRefEtat() && !estAgentDPM()) {
			return false;
		}
		return true;
	}

	private Date calculDateEtHeureSaisie(Date dateJour, Date heureSaisie) {
		if (heureSaisie != null) {
			DateTime result = new DateTime(dateJour);
			return result.plusMinutes(new DateTime(heureSaisie).getMinuteOfDay()).toDate();
		}
		return null;
	}

	private HeureSupDto setHSupARecupererForDPM(HeureSupDto hSupDto) {
		if (estAgentDPM())
			hSupDto.setRecuperee(true);

		return hSupDto;
	}

	private FichePointageDto transformFromSaisiePointageFormToFichePointageDto(SaisiePointageForm form) {

		FichePointageDto dto = getFicheCourante();

		// les absences
		if (null != form.getMapAllAbsence()) {
			Map<String, List<AbsenceDto>> mapAllAbsence = form.getMapAllAbsence();
			// 1ere saisie (1ere ligne)
			int iJour = 0;
			if (0 < mapAllAbsence.size()) {
				for (AbsenceDto absenceDto : mapAllAbsence.get("0")) {
					dto.getSaisies().get(iJour).getAbsences().clear();
					if (null != absenceDto.getIdRefEtat()) {
						absenceDto.setHeureDebut(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureDebut()));
						absenceDto.setHeureFin(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureFin()));

						dto.getSaisies().get(iJour).getAbsences().add(absenceDto);
					}
					iJour++;
				}
			}
			// 2e saisie (2e ligne)
			if (1 < mapAllAbsence.size()) {
				iJour = 0;
				for (AbsenceDto absenceDto : mapAllAbsence.get("1")) {
					if (null != absenceDto.getIdRefEtat()) {
						absenceDto.setHeureDebut(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureDebut()));
						absenceDto.setHeureFin(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								absenceDto.getHeureFin()));

						dto.getSaisies().get(iJour).getAbsences().add(absenceDto);
					}
					iJour++;
				}
			}
		}

		// les heures supp
		if (null != form.getMapAllHSup()) {
			Map<String, List<HeureSupDto>> mapAllHSup = form.getMapAllHSup();
			// 1ere saisie (1ere ligne)
			int iJour = 0;
			if (0 < mapAllHSup.size()) {
				for (HeureSupDto hSupDto : mapAllHSup.get("0")) {
					dto.getSaisies().get(iJour).getHeuresSup().clear();
					if (null != hSupDto.getIdRefEtat()) {
						hSupDto.setHeureDebut(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureDebut()));
						hSupDto.setHeureFin(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureFin()));

						dto.getSaisies().get(iJour).getHeuresSup().add(hSupDto);
					}
					iJour++;
				}
			}
			// 2e saisie (2e ligne)
			if (1 < mapAllHSup.size()) {
				iJour = 0;
				for (HeureSupDto hSupDto : mapAllHSup.get("1")) {
					if (null != hSupDto.getIdRefEtat()) {
						hSupDto.setHeureDebut(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureDebut()));
						hSupDto.setHeureFin(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
								hSupDto.getHeureFin()));

						dto.getSaisies().get(iJour).getHeuresSup().add(hSupDto);
					}
					iJour++;
				}
			}
		}

		// les primes
		// on nettoie l'existant
		for (JourPointageDto jourPtg : dto.getSaisies()) {
			jourPtg.getPrimes().clear();
		}
		// on mappe
		if (null != form.getMapAllPrime()) {
			Map<String, List<PrimeDto>> mapAllPrime = form.getMapAllPrime();

			for (String key : mapAllPrime.keySet()) {
				List<PrimeDto> listPrimes = mapAllPrime.get(key);
				int iJour = 0;
				for (PrimeDto primeDto : listPrimes) {
					if (null != primeDto.getIdRefEtat()) {

						if (periodeHeure(primeDto.getTypeSaisie())) {
							primeDto.setHeureDebut(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
									primeDto.getHeureDebut()));
							primeDto.setHeureFin(calculDateEtHeureSaisie(dto.getSaisies().get(iJour).getDate(),
									primeDto.getHeureFin()));
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

			List<HeureSupDto> newListHsup = new ArrayList<HeureSupDto>();
			for (int i = 0; i < 7; i++) {
				newListHsup.add(new HeureSupDto());
			}

			getSaisiePointageForm().getMapAllHSup().put(new Integer(1).toString(), newListHsup);
		}
	}

	@Command
	@NotifyChange({ "saisiePointageForm" })
	public void ajouterLigneAbsence() {
		if (null != getSaisiePointageForm().getMapAllAbsence() && 2 > getSaisiePointageForm().getMapAllAbsence().size()) {

			List<AbsenceDto> newListAbsence = new ArrayList<AbsenceDto>();
			for (int i = 0; i < 7; i++) {
				newListAbsence.add(new AbsenceDto());
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

	public boolean isAfficherDupliquerPrimeGauche(PrimeDto prime) {
		if (prime.getIdRefEtat() == null) {
			return false;
		}
		if (getSaisiePointageForm() != null) {
			for (int i = 0; i < getFicheCourante().getSaisies().size(); i++) {
				for (PrimeDto dto : getFicheCourante().getSaisies().get(i).getPrimes()) {
					if (getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString()) != null) {
						int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString())
								.indexOf(prime);
						if (indexPart2 == 0) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public boolean isAfficherDupliquerPrimeDroit(PrimeDto prime) {
		if (prime.getIdRefEtat() == null) {
			return false;
		}

		for (int i = 0; i < getSaisiePointageForm().getMapAllPrime().size(); i++) {
			for (PrimeDto dto : getFicheCourante().getSaisies().get(i).getPrimes()) {
				if (getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString()) != null) {
					int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString())
							.indexOf(prime);
					if (indexPart2 == (getSaisiePointageForm().getMapAllPrime().get(dto.getNumRubrique().toString())
							.size() - 1)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isAfficherDupliquerAbsenceGauche(AbsenceDto absence) {
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

	public boolean isAfficherDupliquerAbsenceDroit(AbsenceDto absence) {
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

	public boolean isAfficherDupliquerHSupGauche(HeureSupDto hsup) {
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

	public boolean isAfficherDupliquerHSupDroit(HeureSupDto hsup) {
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
	@NotifyChange({ "*" })
	public void copieDroiteAbsence(@BindingParam("ref") AbsenceDto absence) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllAbsence().size(); i++) {
			if (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).indexOf(absence);
				if (indexPart2 != -1) {
					AbsenceDto absSuiv = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i))
							.get(indexPart2 + 1);
					ajouterAbsence(absSuiv);
					// on copie les données
					absSuiv.setCommentaire(absence.getCommentaire());
					absSuiv.setMotif(absence.getMotif());
					absSuiv.setHeureDebut(absence.getHeureDebut());
					absSuiv.setHeureFin(absence.getHeureFin());
					absSuiv.setIdRefTypeAbsence(absence.getIdRefTypeAbsence());
					setHasTextChanged(true);
				}
			}
		}
	}

	@Command
	@NotifyChange({ "*" })
	public void copieGaucheAbsence(@BindingParam("ref") AbsenceDto absence) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllAbsence().size(); i++) {
			if (getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i)).indexOf(absence);
				if (indexPart2 != -1) {
					AbsenceDto absPrec = getSaisiePointageForm().getMapAllAbsence().get(String.valueOf(i))
							.get(indexPart2 - 1);
					ajouterAbsence(absPrec);
					// on copie les données
					absPrec.setCommentaire(absence.getCommentaire());
					absPrec.setMotif(absence.getMotif());
					absPrec.setHeureDebut(absence.getHeureDebut());
					absPrec.setHeureFin(absence.getHeureFin());
					absPrec.setIdRefTypeAbsence(absence.getIdRefTypeAbsence());
					setHasTextChanged(true);
				}
			}
		}
	}

	@Command
	@NotifyChange({ "*" })
	public void copieDroiteHSup(@BindingParam("ref") HeureSupDto hsup) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllHSup().size(); i++) {
			if (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).indexOf(hsup);
				if (indexPart2 != -1) {
					HeureSupDto hsupSuiv = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i))
							.get(indexPart2 + 1);
					ajouterHSup(hsupSuiv);
					// on copie les données
					hsupSuiv.setCommentaire(hsup.getCommentaire());
					hsupSuiv.setIdMotifHsup(hsup.getIdMotifHsup());
					hsupSuiv.setHeureDebut(hsup.getHeureDebut());
					hsupSuiv.setHeureFin(hsup.getHeureFin());
					hsupSuiv.setRappelService(hsup.isRappelService());
					hsupSuiv.setRecuperee(hsup.isRecuperee());
					setHasTextChanged(true);
				}
			}
		}
	}

	@Command
	@NotifyChange({ "*" })
	public void copieGaucheHSup(@BindingParam("ref") HeureSupDto hsup) {
		for (int i = 0; i < getSaisiePointageForm().getMapAllHSup().size(); i++) {
			if (getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)) != null) {
				int indexPart2 = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i)).indexOf(hsup);
				if (indexPart2 != -1) {
					HeureSupDto hsupPrec = getSaisiePointageForm().getMapAllHSup().get(String.valueOf(i))
							.get(indexPart2 - 1);
					ajouterHSup(hsupPrec);
					// on copie les données
					hsupPrec.setCommentaire(hsup.getCommentaire());
					hsupPrec.setIdMotifHsup(hsup.getIdMotifHsup());
					hsupPrec.setHeureDebut(hsup.getHeureDebut());
					hsupPrec.setHeureFin(hsup.getHeureFin());
					hsupPrec.setRappelService(hsup.isRappelService());
					hsupPrec.setRecuperee(hsup.isRecuperee());
					setHasTextChanged(true);
				}
			}
		}
	}

	@Command
	@NotifyChange({ "*" })
	public void copieDroitePrime(@BindingParam("ref") PrimeDto prime) {
		if (getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString()) != null) {
			int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.indexOf(prime);
			PrimeDto primeSuiv = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.get(indexPart2 + 1);
			ajouterPrime(primeSuiv);
			// on copie les données
			primeSuiv.setCommentaire(prime.getCommentaire());
			primeSuiv.setMotif(prime.getMotif());
			primeSuiv.setHeureDebut(prime.getHeureDebut());
			primeSuiv.setHeureFin(prime.getHeureFin());
			primeSuiv.setQuantite(prime.getQuantite());
			setHasTextChanged(true);
		}

	}

	@Command
	@NotifyChange({ "*" })
	public void copieGauchePrime(@BindingParam("ref") PrimeDto prime) {
		if (getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString()) != null) {
			int indexPart2 = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.indexOf(prime);
			PrimeDto primePrec = getSaisiePointageForm().getMapAllPrime().get(prime.getNumRubrique().toString())
					.get(indexPart2 - 1);
			ajouterPrime(primePrec);
			// on copie les données
			primePrec.setCommentaire(prime.getCommentaire());
			primePrec.setMotif(prime.getMotif());
			primePrec.setHeureDebut(prime.getHeureDebut());
			primePrec.setHeureFin(prime.getHeureFin());
			primePrec.setQuantite(prime.getQuantite());
			setHasTextChanged(true);
		}

	}

	public ListModel<MotifHeureSupDto> getListeMotifHsup() {
		return listeMotifHsup;
	}

	public void setListeMotifHsup(ListModel<MotifHeureSupDto> listeMotifHsup) {
		this.listeMotifHsup = listeMotifHsup;
	}

}
