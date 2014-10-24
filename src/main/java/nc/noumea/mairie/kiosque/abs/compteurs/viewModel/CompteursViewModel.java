package nc.noumea.mairie.kiosque.abs.compteurs.viewModel;

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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.CompteurDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.MotifCompteurDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceEnum;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CompteursViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private String formulaireRecup;

	private String formulaireReposComp;

	private List<MotifCompteurDto> listeMotifsCompteur;

	private MotifCompteurDto motifCompteur;

	private SoldeDto soldeCourant;

	private String nouveauSolde;

	private String nouveauSoldeAnneePrec;

	private CompteurDto compteurACreer;

	private String anneePrec;

	/* Pour les filtres */

	private List<RefTypeAbsenceDto> listeTypeAbsenceFiltre;

	private RefTypeAbsenceDto typeAbsenceFiltre;

	private List<ServiceDto> listeServicesFiltre;

	private ServiceDto serviceFiltre;

	private List<AgentDto> listeAgentsFiltre;

	private AgentDto agentFiltre;

	private ProfilAgentDto currentUser;

	@Init
	public void initCompteurs() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on charge les types d'absences pour les filtres
		List<RefTypeAbsenceDto> filtreFamille = absWsConsumer.getRefGroupeAbsenceCompteur();
		setListeTypeAbsenceFiltre(filtreFamille);
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = absWsConsumer.getServicesAbsences(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		// pour les agents, on ne rempli pas la liste, elle le sera avec le
		// choix du service
		setListeAgentsFiltre(null);
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void chargeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = absWsConsumer.getAgentsAbsences(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	@Command
	@NotifyChange({ "formulaireRecup", "formulaireReposComp", "motifCompteur", "soldeCourant" })
	public void refreshNomAgent() {
		setFormulaireRecup(null);
		setFormulaireReposComp(null);
		setMotifCompteur(null);
		setSoldeCourant(null);
		if (getTypeAbsenceFiltre().getIdRefTypeAbsence() == RefTypeAbsenceEnum.RECUP.getValue()) {
			// juste pour afficher le nom de l'agent dans le formulaire
			setFormulaireRecup("Solde du compteur de récupération pour l'agent " + getAgentFiltre().getNom() + " "
					+ getAgentFiltre().getPrenom());
			FiltreSoldeDto filtreDto = new FiltreSoldeDto();
			filtreDto.setDateDebut(new Date());
			filtreDto.setDateFin(new Date());
			SoldeDto result = absWsConsumer.getAgentSolde(getAgentFiltre().getIdAgent(), filtreDto);
			setSoldeCourant(result);
		} else if (getTypeAbsenceFiltre().getIdRefTypeAbsence() == RefTypeAbsenceEnum.REPOS_COMP.getValue()) {
			setFormulaireReposComp("Solde du compteur de repos compensateur pour l'agent " + getAgentFiltre().getNom()
					+ " " + getAgentFiltre().getPrenom());
			FiltreSoldeDto filtreDto = new FiltreSoldeDto();
			filtreDto.setDateDebut(new Date());
			filtreDto.setDateFin(new Date());
			SoldeDto result = absWsConsumer.getAgentSolde(getAgentFiltre().getIdAgent(), filtreDto);
			setSoldeCourant(result);
		}
	}

	@Command
	@NotifyChange({ "typeAbsenceFiltre", "serviceFiltre", "agentFiltre", "formulaireRecup", "formulaireReposComp",
			"listeMotifsCompteur", "motifCompteur", "soldeCourant", "nouveauSolde", "compteurACreer",
			"nouveauSoldeAnneePrec" })
	public void chargeFormulaire() {
		videFormulaireSansType();
		if (getTypeAbsenceFiltre().getIdRefTypeAbsence() == RefTypeAbsenceEnum.RECUP.getValue()) {
			setAnneePrec("0");
			if (getAgentFiltre() == null) {
				setFormulaireRecup("Solde du compteur de récupération pour l'agent ");
			} else {
				setFormulaireRecup("Solde du compteur de récupération pour l'agent " + getAgentFiltre().getNom() + " "
						+ getAgentFiltre().getPrenom());
				FiltreSoldeDto filtreDto = new FiltreSoldeDto();
				filtreDto.setDateDebut(new Date());
				filtreDto.setDateFin(new Date());
				SoldeDto result = absWsConsumer.getAgentSolde(getAgentFiltre().getIdAgent(), filtreDto);
				setSoldeCourant(result);
			}
			// on charge la liste des motifs d'alimenation des compteurs
			setListeMotifsCompteur(absWsConsumer.getListeMotifsCompteur(getTypeAbsenceFiltre().getIdRefTypeAbsence()));
			setCompteurACreer(new CompteurDto());

		} else if (getTypeAbsenceFiltre().getIdRefTypeAbsence() == RefTypeAbsenceEnum.REPOS_COMP.getValue()) {
			setAnneePrec("0");
			if (getAgentFiltre() == null) {
				setFormulaireReposComp("Solde du compteur de repos compensateur pour l'agent ");
			} else {
				setFormulaireReposComp("Solde du compteur de repos compensateur pour l'agent "
						+ getAgentFiltre().getNom() + " " + getAgentFiltre().getPrenom());
				FiltreSoldeDto filtreDto = new FiltreSoldeDto();
				filtreDto.setDateDebut(new Date());
				filtreDto.setDateFin(new Date());
				SoldeDto result = absWsConsumer.getAgentSolde(getAgentFiltre().getIdAgent(), filtreDto);
				setSoldeCourant(result);
			}
			// on charge la liste des motifs d'alimenation des compteurs
			setListeMotifsCompteur(absWsConsumer.getListeMotifsCompteur(getTypeAbsenceFiltre().getIdRefTypeAbsence()));
			setCompteurACreer(new CompteurDto());
		}
	}

	@Command
	@NotifyChange({ "nouveauSolde", "nouveauSoldeAnneePrec" })
	public void actualiseNouveauSoldeRecup(@BindingParam("ref") String texte) {
		setNouveauSolde(null);
		setNouveauSoldeAnneePrec(null);
		if (texte != null && texte.equals("ajouter")) {
			// on recupere la durée
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String dureeAjout = getCompteurACreer().getDateDebut() == null ? "00:00" : sdf.format(getCompteurACreer()
					.getDateDebut());
			// on transforme ce temps en minute
			String heures = dureeAjout.substring(0, dureeAjout.indexOf(":"));
			String minutes = dureeAjout.substring(dureeAjout.indexOf(":") + 1, dureeAjout.length());
			// on rempli le DTO avec la valeur
			Double dureeAj = (double) ((Integer.valueOf(heures) * 60) + Integer.valueOf(minutes));
			getCompteurACreer().setDureeAAjouter(dureeAj == 0 ? null : dureeAj);
			// on affiche le nouveau solde
			setNouveauSolde(getHeureMinute(getSoldeCourant().getSoldeRecup(), getCompteurACreer()));
		} else if (texte != null && texte.equals("retrancher")) {
			// on recupere la durée
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String dureeRetranche = getCompteurACreer().getDateFin() == null ? "00:00" : sdf.format(getCompteurACreer()
					.getDateFin());
			// on transforme ce temps en minute
			String heures = dureeRetranche.substring(0, dureeRetranche.indexOf(":"));
			String minutes = dureeRetranche.substring(dureeRetranche.indexOf(":") + 1, dureeRetranche.length());
			// on rempli le DTO avec la valeur
			Double dureeRetr = (double) ((Integer.valueOf(heures) * 60) + Integer.valueOf(minutes));
			getCompteurACreer().setDureeARetrancher(dureeRetr == 0 ? null : dureeRetr);
			// on affiche le nouveau solde
			setNouveauSolde(getHeureMinute(getSoldeCourant().getSoldeRecup(), getCompteurACreer()));
		}
	}

	@Command
	@NotifyChange({ "nouveauSolde", "nouveauSoldeAnneePrec" })
	public void changeAnneePrec() {
		setNouveauSolde(null);
		setNouveauSoldeAnneePrec(null);
		if (getSoldeCourant() != null) {
			// on affiche le nouveau solde
			if (compteurAnneePrec())
				setNouveauSoldeAnneePrec(getHeureMinute(getSoldeCourant().getSoldeReposCompAnneePrec(),
						getCompteurACreer()));
			else
				setNouveauSolde(getHeureMinute(getSoldeCourant().getSoldeReposCompAnnee(), getCompteurACreer()));
		}
	}

	@Command
	@NotifyChange({ "nouveauSolde", "nouveauSoldeAnneePrec" })
	public void actualiseNouveauSoldeReposComp(@BindingParam("ref") String texte) {
		if (getSoldeCourant() != null) {

			setNouveauSolde(null);
			setNouveauSoldeAnneePrec(null);
			if (texte != null && texte.equals("ajouter")) {
				// on recupere la durée
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String dureeAjout = getCompteurACreer().getDateDebut() == null ? "00:00" : sdf
						.format(getCompteurACreer().getDateDebut());
				// on transforme ce temps en minute
				String heures = dureeAjout.substring(0, dureeAjout.indexOf(":"));
				String minutes = dureeAjout.substring(dureeAjout.indexOf(":") + 1, dureeAjout.length());
				// on rempli le DTO avec la valeur
				Double dureeAj = (double) ((Integer.valueOf(heures) * 60) + Integer.valueOf(minutes));
				getCompteurACreer().setDureeAAjouter(dureeAj == 0 ? null : dureeAj);
				// on affiche le nouveau solde
				if (compteurAnneePrec())
					setNouveauSoldeAnneePrec(getHeureMinute(getSoldeCourant().getSoldeReposCompAnneePrec(),
							getCompteurACreer()));
				else
					setNouveauSolde(getHeureMinute(getSoldeCourant().getSoldeReposCompAnnee(), getCompteurACreer()));
			} else if (texte != null && texte.equals("retrancher")) {
				// on recupere la durée
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String dureeRetranche = getCompteurACreer().getDateFin() == null ? "00:00" : sdf
						.format(getCompteurACreer().getDateFin());
				// on transforme ce temps en minute
				String heures = dureeRetranche.substring(0, dureeRetranche.indexOf(":"));
				String minutes = dureeRetranche.substring(dureeRetranche.indexOf(":") + 1, dureeRetranche.length());
				// on rempli le DTO avec la valeur
				Double dureeRetr = (double) ((Integer.valueOf(heures) * 60) + Integer.valueOf(minutes));
				getCompteurACreer().setDureeARetrancher(dureeRetr == 0 ? null : dureeRetr);
				// on affiche le nouveau solde
				if (compteurAnneePrec())
					setNouveauSoldeAnneePrec(getHeureMinute(getSoldeCourant().getSoldeReposCompAnneePrec(),
							getCompteurACreer()));
				else
					setNouveauSolde(getHeureMinute(getSoldeCourant().getSoldeReposCompAnnee(), getCompteurACreer()));
			}
		}
	}

	public String soldeJour(Double solde) {
		if (solde == 0)
			return "aucun";
		return solde + " j";
	}

	public String soldeHeure(Double solde) {
		if (solde == 0)
			return "aucun";
		return getHeureMinute(solde.intValue());
	}

	private static String getHeureMinute(int nombreMinute) {
		int heure = nombreMinute / 60;
		int minute = nombreMinute % 60;
		String res = "";
		if (heure > 0)
			res += heure + "h";
		if (minute > 0)
			res += minute + "m";

		return res;
	}

	private boolean compteurAnneePrec() {
		return getAnneePrec() == null ? false : getAnneePrec().equals("1") ? true : false;
	}

	private String getHeureMinute(Double soldeExistant, CompteurDto compteurACreer) {
		Integer nombreMinute = (int) (soldeExistant
				+ (compteurACreer.getDureeAAjouter() == null ? 0 : compteurACreer.getDureeAAjouter()) - (compteurACreer
				.getDureeARetrancher() == null ? 0 : compteurACreer.getDureeARetrancher()));

		return getHeureMinute(nombreMinute);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	@Command
	@NotifyChange({ "typeAbsenceFiltre", "serviceFiltre", "agentFiltre", "formulaireRecup", "formulaireReposComp",
			"listeMotifsCompteur", "motifCompteur", "soldeCourant", "nouveauSolde", "compteurACreer",
			"nouveauSoldeAnneePrec" })
	public void saveCompteurRecup() {

		if (IsFormValid(getCompteurACreer())) {
			getCompteurACreer().setDateDebut(null);
			getCompteurACreer().setDateFin(null);
			getCompteurACreer().setAnneePrecedente(false);
			getCompteurACreer().setIdAgent(getAgentFiltre().getIdAgent());
			getCompteurACreer().setIdMotifCompteur(getMotifCompteur().getIdMotifCompteur());
			getCompteurACreer().setIdOrganisationSyndicale(null);

			ReturnMessageDto result = absWsConsumer.saveCompteurRecup(currentUser.getAgent().getIdAgent(),
					getCompteurACreer());

			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
			// ici la liste info est toujours vide alors on ajoute un
			// message
			if (result.getErrors().size() == 0)
				result.getInfos().add(
						"Le compteur de récupération de l'agent " + getAgentFiltre().getNom() + " "
								+ getAgentFiltre().getPrenom() + " a été enregistré correctement.");
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

			// on reinitialise le formulaire
			videFormulaire();
		}

	}

	@Command
	@NotifyChange({ "typeAbsenceFiltre", "serviceFiltre", "agentFiltre", "formulaireRecup", "formulaireReposComp",
			"listeMotifsCompteur", "motifCompteur", "soldeCourant", "nouveauSolde", "compteurACreer",
			"nouveauSoldeAnneePrec" })
	public void saveCompteurReposComp() {

		if (IsFormValid(getCompteurACreer())) {
			getCompteurACreer().setDateDebut(null);
			getCompteurACreer().setDateFin(null);
			getCompteurACreer().setAnneePrecedente(compteurAnneePrec());
			getCompteurACreer().setIdAgent(getAgentFiltre().getIdAgent());
			getCompteurACreer().setIdMotifCompteur(getMotifCompteur().getIdMotifCompteur());
			getCompteurACreer().setIdOrganisationSyndicale(null);

			ReturnMessageDto result = absWsConsumer.saveCompteurReposComp(currentUser.getAgent().getIdAgent(),
					getCompteurACreer());

			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
			// ici la liste info est toujours vide alors on ajoute un
			// message
			if (result.getErrors().size() == 0)
				result.getInfos().add(
						"Le compteur de repos compensateur de l'agent " + getAgentFiltre().getNom() + " "
								+ getAgentFiltre().getPrenom() + " a été enregistré correctement.");
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

			// on reinitialise le formulaire
			videFormulaire();
		}

	}

	private boolean IsFormValid(CompteurDto compteur) {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Durée
		if (compteur.getDureeAAjouter() == null && compteur.getDureeARetrancher() == null) {
			vList.add(new ValidationMessage("La durée est obligatoire."));
		}
		if (compteur.getDureeAAjouter() != null && compteur.getDureeARetrancher() != null) {
			if (compteur.getDureeAAjouter() != 0 && compteur.getDureeARetrancher() != 0) {
				vList.add(new ValidationMessage("Une seule durée doit être choisie."));
			}
		}

		// compteur
		if (getMotifCompteur() == null) {
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

	private void videFormulaire() {
		videFormulaireSansType();
		setTypeAbsenceFiltre(null);
	}

	private void videFormulaireSansType() {
		setAnneePrec("0");
		setFormulaireRecup(null);
		setFormulaireReposComp(null);
		setListeMotifsCompteur(null);
		setMotifCompteur(null);
		setSoldeCourant(null);
		setNouveauSolde(null);
		setNouveauSoldeAnneePrec(null);
		setCompteurACreer(null);
		setAgentFiltre(null);
		setServiceFiltre(null);
	}

	public List<RefTypeAbsenceDto> getListeTypeAbsenceFiltre() {
		return listeTypeAbsenceFiltre;
	}

	public void setListeTypeAbsenceFiltre(List<RefTypeAbsenceDto> listeTypeAbsenceFiltre) {
		this.listeTypeAbsenceFiltre = listeTypeAbsenceFiltre;
	}

	public RefTypeAbsenceDto getTypeAbsenceFiltre() {
		return typeAbsenceFiltre;
	}

	public void setTypeAbsenceFiltre(RefTypeAbsenceDto typeAbsenceFiltre) {
		this.typeAbsenceFiltre = typeAbsenceFiltre;
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

	public String getFormulaireRecup() {
		return formulaireRecup;
	}

	public void setFormulaireRecup(String formulaireRecup) {
		this.formulaireRecup = formulaireRecup;
	}

	public String getFormulaireReposComp() {
		return formulaireReposComp;
	}

	public void setFormulaireReposComp(String formulaireReposComp) {
		this.formulaireReposComp = formulaireReposComp;
	}

	public List<MotifCompteurDto> getListeMotifsCompteur() {
		return listeMotifsCompteur;
	}

	public void setListeMotifsCompteur(List<MotifCompteurDto> listeMotifsCompteur) {
		this.listeMotifsCompteur = listeMotifsCompteur;
	}

	public MotifCompteurDto getMotifCompteur() {
		return motifCompteur;
	}

	public void setMotifCompteur(MotifCompteurDto motifCompteur) {
		this.motifCompteur = motifCompteur;
	}

	public SoldeDto getSoldeCourant() {
		return soldeCourant;
	}

	public void setSoldeCourant(SoldeDto soldeCourant) {
		this.soldeCourant = soldeCourant;
	}

	public CompteurDto getCompteurACreer() {
		return compteurACreer;
	}

	public void setCompteurACreer(CompteurDto compteurACreer) {
		this.compteurACreer = compteurACreer;
	}

	public String getNouveauSolde() {
		return nouveauSolde;
	}

	public void setNouveauSolde(String nouveauSolde) {
		this.nouveauSolde = nouveauSolde;
	}

	public String getNouveauSoldeAnneePrec() {
		return nouveauSoldeAnneePrec;
	}

	public void setNouveauSoldeAnneePrec(String nouveauSoldeAnneePrec) {
		this.nouveauSoldeAnneePrec = nouveauSoldeAnneePrec;
	}

	public String getAnneePrec() {
		return anneePrec;
	}

	public void setAnneePrec(String anneePrec) {
		this.anneePrec = anneePrec;
	}

}
