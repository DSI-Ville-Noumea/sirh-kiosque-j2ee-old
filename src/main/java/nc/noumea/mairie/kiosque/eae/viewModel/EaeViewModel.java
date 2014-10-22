package nc.noumea.mairie.kiosque.eae.viewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAppreciationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeObjectifDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeObjectifProDto;
import nc.noumea.mairie.kiosque.eae.dto.EaePlanActionDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeResultatDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeSouhaitDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EaeViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private EaeListItemDto eaeCourant;

	private Tab tabCourant;

	private Tabbox tabboxCourant;

	private EaeIdentificationDto identification;

	private List<EaeFichePosteDto> listeFichePoste;

	private EaeFichePosteDto fichePostePrimaire;

	private EaeFichePosteDto fichePosteSecondaire;

	private EaeResultatDto resultat;

	private EaeAppreciationDto appreciationAnnee;

	private EaeAppreciationDto appreciationAnneePrec;

	private Integer annee;

	private Integer anneePrec;

	private EaeEvaluationDto evaluation;

	private Date dureeEntretien;

	private EaeAutoEvaluationDto autoEvaluation;

	private EaePlanActionDto planAction;

	private EaeEvolutionDto evolution;

	/* Pour savoir si on est en modif ou en visu */
	private String modeSaisi;
	private boolean isModification;
	/* Pour savoir si on affiche la disquette de sauvegarde */
	private boolean hasTextChanged;

	@Init
	public void initEae(@ExecutionArgParam("eae") EaeListItemDto eae, @ExecutionArgParam("mode") String modeSaisi) {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		setEaeCourant(eae);

		setModeSaisi(modeSaisi);
		setModification(modeSaisi.equals("EDIT"));

		// on charge l'identification
		initIdentification();
		// on charge les fiches de poste
		initFichePoste();
		// on charge les resultats
		initResultat();
		// on charge les appreciations
		initAppreciation();
		// on charge l'évaluation
		initEvaluation();
		// on charge l'auto-évaluation
		initAutoEvaluation();
		// on charge le plan d'action
		initPlanAction();
		// on charge l'evolution
		initEvolution();
	}

	private void initEvolution() {
		EaeEvolutionDto evo = eaeWsConsumer.getEvolutionEae(getEaeCourant().getIdEae(), currentUser.getAgent()
				.getIdAgent());
		setEvolution(evo);
	}

	private void initPlanAction() {
		EaePlanActionDto plan = eaeWsConsumer.getPlanActionEae(getEaeCourant().getIdEae(), currentUser.getAgent()
				.getIdAgent());
		setPlanAction(plan);
	}

	private void initAutoEvaluation() {
		EaeAutoEvaluationDto autoEvaluation = eaeWsConsumer.getAutoEvaluationEae(getEaeCourant().getIdEae(),
				currentUser.getAgent().getIdAgent());
		setAutoEvaluation(autoEvaluation);
	}

	private void initEvaluation() {
		EaeEvaluationDto evaluation = eaeWsConsumer.getEvaluationEae(getEaeCourant().getIdEae(), currentUser.getAgent()
				.getIdAgent());
		setEvaluation(evaluation);
	}

	private void initAppreciation() {
		EaeAppreciationDto appreciationAnnee = eaeWsConsumer.getAppreciationEae(getEaeCourant().getIdEae(), currentUser
				.getAgent().getIdAgent(), eaeWsConsumer.getCampagneEae().getAnnee());
		setAppreciationAnnee(appreciationAnnee);
		// on charge les appreciations de l'annéee précédente
		EaeAppreciationDto appreciationAnneePrec = eaeWsConsumer.getAppreciationEae(getEaeCourant().getIdEae(),
				currentUser.getAgent().getIdAgent(), eaeWsConsumer.getCampagneEae().getAnnee() - 1);
		setAppreciationAnneePrec(appreciationAnneePrec);

		setAnnee(eaeWsConsumer.getCampagneEae().getAnnee());
		setAnneePrec(eaeWsConsumer.getCampagneEae().getAnnee() - 1);
	}

	private void initResultat() {
		EaeResultatDto resultat = eaeWsConsumer.getResultatEae(getEaeCourant().getIdEae(), currentUser.getAgent()
				.getIdAgent());
		setResultat(resultat);
	}

	private void initFichePoste() {
		List<EaeFichePosteDto> listeFDP = eaeWsConsumer.getListeFichePosteEae(getEaeCourant().getIdEae(), currentUser
				.getAgent().getIdAgent());
		setListeFichePoste(listeFDP);
		if (getListeFichePoste().size() == 1) {
			setFichePostePrimaire(getListeFichePoste().get(0));
		} else if (getListeFichePoste().size() == 2) {
			setFichePostePrimaire(getListeFichePoste().get(0));
			setFichePosteSecondaire(getListeFichePoste().get(1));
		}
	}

	private void initIdentification() {
		EaeIdentificationDto identification = eaeWsConsumer.getIdentificationEae(getEaeCourant().getIdEae(),
				currentUser.getAgent().getIdAgent());
		setIdentification(identification);
	}

	@GlobalCommand
	@Command
	@NotifyChange({ "hasTextChanged", "identification", "resultat", "appreciationAnnee", "evaluation",
			"autoEvaluation", "planAction", "evolution" })
	public void engistreOnglet() {
		// on sauvegarde l'onglet
		ReturnMessageDto result = new ReturnMessageDto();
		if (getTabCourant().getId().equals("IDENTIFICATION")) {
			result = eaeWsConsumer.saveIdentification(getIdentification().getIdEae(), currentUser.getAgent()
					.getIdAgent(), getIdentification());
		} else if (getTabCourant().getId().equals("RESULTAT")) {
			result = eaeWsConsumer.saveResultat(getResultat().getIdEae(), currentUser.getAgent().getIdAgent(),
					getResultat());
		} else if (getTabCourant().getId().equals("APPRECIATION")) {
			result = eaeWsConsumer.saveAppreciation(getResultat().getIdEae(), currentUser.getAgent().getIdAgent(),
					getAppreciationAnnee());
		} else if (getTabCourant().getId().equals("EVALUATION")) {
			result = eaeWsConsumer.saveEvaluation(getResultat().getIdEae(), currentUser.getAgent().getIdAgent(),
					getEvaluation());
		} else if (getTabCourant().getId().equals("AUTOEVALUATION")) {
			result = eaeWsConsumer.saveAutoEvaluation(getResultat().getIdEae(), currentUser.getAgent().getIdAgent(),
					getAutoEvaluation());
		} else if (getTabCourant().getId().equals("PLANACTION")) {
			result = eaeWsConsumer.savePlanAction(getResultat().getIdEae(), currentUser.getAgent().getIdAgent(),
					getPlanAction());
		} else if (getTabCourant().getId().equals("EVOLUTION")) {
			result = eaeWsConsumer.saveEvolution(getResultat().getIdEae(), currentUser.getAgent().getIdAgent(),
					getEvolution());
		}

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
			setHasTextChanged(false);

			// on recharge l'eae pour vider les eventuelles modifs
			initEae(getEaeCourant(), getModeSaisi());
		}
	}

	@Command
	@NotifyChange({ "hasTextChanged", "evolution" })
	public void supprimerLigneSouhaitSuggestion(@BindingParam("ref") EaeSouhaitDto souhait) {
		if (getEvolution().getSouhaitsSuggestions().contains(souhait)) {
			getEvolution().getSouhaitsSuggestions().remove(souhait);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "evolution" })
	public void ajouterLigneSouhaitSuggestion() {
		EaeSouhaitDto dto = new EaeSouhaitDto();
		if (getEvolution().getSouhaitsSuggestions() != null) {
			getEvolution().getSouhaitsSuggestions().add(dto);
		} else {
			List<EaeSouhaitDto> liste = new ArrayList<>();
			liste.add(dto);
			getEvolution().setSouhaitsSuggestions(liste);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void supprimerLigneMoyensAutres(@BindingParam("ref") String moyensAutres) {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getMoyensAutres().length; i++) {
			wordList.add(getPlanAction().getMoyensAutres()[i]);
		}

		if (wordList.contains(moyensAutres)) {
			wordList.remove(moyensAutres);
		}
		getPlanAction().setMoyensAutres(wordList.toArray(new String[wordList.size()]));
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void ajouterLigneMoyensAutres() {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getMoyensAutres().length; i++) {
			wordList.add(getPlanAction().getMoyensAutres()[i]);
		}

		wordList.add(new String());
		getPlanAction().setMoyensAutres(wordList.toArray(new String[wordList.size()]));

		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void supprimerLigneMoyensFinanciers(@BindingParam("ref") String moyensFinanciers) {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getMoyensFinanciers().length; i++) {
			wordList.add(getPlanAction().getMoyensFinanciers()[i]);
		}

		if (wordList.contains(moyensFinanciers)) {
			wordList.remove(moyensFinanciers);
		}
		getPlanAction().setMoyensFinanciers(wordList.toArray(new String[wordList.size()]));
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void ajouterLigneMoyensFinanciers() {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getMoyensFinanciers().length; i++) {
			wordList.add(getPlanAction().getMoyensFinanciers()[i]);
		}

		wordList.add(new String());
		getPlanAction().setMoyensFinanciers(wordList.toArray(new String[wordList.size()]));

		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void supprimerLigneMoyensMateriels(@BindingParam("ref") String moyensMateriels) {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getMoyensMateriels().length; i++) {
			wordList.add(getPlanAction().getMoyensMateriels()[i]);
		}

		if (wordList.contains(moyensMateriels)) {
			wordList.remove(moyensMateriels);
		}
		getPlanAction().setMoyensMateriels(wordList.toArray(new String[wordList.size()]));
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void ajouterLigneMoyensMateriels() {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getMoyensMateriels().length; i++) {
			wordList.add(getPlanAction().getMoyensMateriels()[i]);
		}

		wordList.add(new String());
		getPlanAction().setMoyensMateriels(wordList.toArray(new String[wordList.size()]));

		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void supprimerLigneObjectifIndiv(@BindingParam("ref") String objectifIndiv) {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getObjectifsIndividuels().length; i++) {
			wordList.add(getPlanAction().getObjectifsIndividuels()[i]);
		}

		if (wordList.contains(objectifIndiv)) {
			wordList.remove(objectifIndiv);
		}
		getPlanAction().setObjectifsIndividuels(wordList.toArray(new String[wordList.size()]));
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void ajouterLigneObjectifIndiv() {
		// on transforme String[] en liste
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < getPlanAction().getObjectifsIndividuels().length; i++) {
			wordList.add(getPlanAction().getObjectifsIndividuels()[i]);
		}

		wordList.add(new String());
		getPlanAction().setObjectifsIndividuels(wordList.toArray(new String[wordList.size()]));

		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void supprimerLigneObjectifPro(@BindingParam("ref") EaeObjectifProDto objectifPro) {
		if (getPlanAction().getObjectifsProfessionnels().contains(objectifPro)) {
			getPlanAction().getObjectifsProfessionnels().remove(objectifPro);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "planAction" })
	public void ajouterLigneObjectifPro() {
		EaeObjectifProDto dto = new EaeObjectifProDto();
		if (getPlanAction().getObjectifsProfessionnels() != null) {
			getPlanAction().getObjectifsProfessionnels().add(dto);
		} else {
			List<EaeObjectifProDto> liste = new ArrayList<>();
			liste.add(dto);
			getPlanAction().setObjectifsProfessionnels(liste);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "resultat" })
	public void supprimerLigneIndiv(@BindingParam("ref") EaeObjectifDto objectifIndiv) {
		if (getResultat().getObjectifsIndividuels().contains(objectifIndiv)) {
			getResultat().getObjectifsIndividuels().remove(objectifIndiv);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "resultat" })
	public void ajouterLigneIndiv() {
		EaeObjectifDto dto = new EaeObjectifDto();
		if (getResultat().getObjectifsIndividuels() != null) {
			getResultat().getObjectifsIndividuels().add(dto);
		} else {
			List<EaeObjectifDto> liste = new ArrayList<>();
			liste.add(dto);
			getResultat().setObjectifsIndividuels(liste);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "resultat" })
	public void supprimerLignePro(@BindingParam("ref") EaeObjectifDto objectifPro) {
		if (getResultat().getObjectifsProfessionnels().contains(objectifPro)) {
			getResultat().getObjectifsProfessionnels().remove(objectifPro);
		}
		textChanged();
	}

	@Command
	@NotifyChange({ "hasTextChanged", "resultat" })
	public void ajouterLignePro() {
		EaeObjectifDto dto = new EaeObjectifDto();
		if (getResultat().getObjectifsProfessionnels() != null) {
			getResultat().getObjectifsProfessionnels().add(dto);
		} else {
			List<EaeObjectifDto> liste = new ArrayList<>();
			liste.add(dto);
			getResultat().setObjectifsProfessionnels(liste);
		}
		textChanged();
	}

	@GlobalCommand
	@NotifyChange({ "hasTextChanged", "identification", "resultat", "appreciationAnnee", "evaluation",
			"autoEvaluation", "planAction", "evolution" })
	public void annulerEngistreOnglet(@BindingParam("tab") Tab tab) {
		setHasTextChanged(false);
		setTabCourant(tab);
		getTabboxCourant().setSelectedTab(getTabCourant());
		// on recharge l'eae pour vider les eventuelles modifs
		initEae(getEaeCourant(), getModeSaisi());
	}

	@Command
	public void changeVue(@BindingParam("tabbox") Tabbox tabBox, @BindingParam("tab") Tab tab) {
		setTabboxCourant(tabBox);
		// on regarde si il y des choses non sauvegardées
		if (isHasTextChanged()) {
			tabBox.setSelectedTab(getTabCourant());
			List<ValidationMessage> vList = new ArrayList<ValidationMessage>();
			vList.add(new ValidationMessage("L'onglet " + getTabCourant().getId()
					+ " semble avoir été modifié, vous devriez l'enregistrer avant de continuer."));
			vList.add(new ValidationMessage("Si vous ne l'enregistrez pas, vous allez perdre vos données."));

			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			map.put("tab", tab);
			Executions.createComponents("/eae/messageEae.zul", null, map);

		} else {
			// on sauvegarde l'onglet
			setTabCourant(tab);
			tabBox.setSelectedTab(getTabCourant());
		}
	}

	@Command
	@NotifyChange({ "hasTextChanged" })
	public void textChanged() {
		if (!isModification())
			setHasTextChanged(false);
		setHasTextChanged(true);
	}

	public String getDateToString(Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}

	public String getInfoResponsable(EaeFichePosteDto fichePoste) {
		if (fichePoste == null)
			return "";
		return fichePoste.getResponsableNom() + " " + fichePoste.getResponsablePrenom() + ", "
				+ fichePoste.getResponsableFonction();
	}

	public String getEnteteN1() {
		return String.valueOf(getAnnee() - 2);
	}

	public String getEnteteN2() {
		return String.valueOf(getAnnee() - 3);
	}

	public String getEnteteN3() {
		return String.valueOf(getAnnee() - 4);
	}

	public String getInfoDureeEntretien(EaeEvaluationDto dto) {
		String res = "";
		if (dto.getDureeEntretien().getHeures() != 0) {
			res += dto.getDureeEntretien().getHeures() + " h ";
		}
		if (dto.getDureeEntretien().getMinutes() != 0) {
			res += dto.getDureeEntretien().getMinutes() + " min";
		}
		return res;
	}

	public String transformeDuree(Integer ancienneteEchelonJours) {
		String res = "";
		if (ancienneteEchelonJours == null)
			return res;

		Integer annee = ancienneteEchelonJours / 365;
		Integer mois = (ancienneteEchelonJours % 365) / 30;
		Integer jour = (ancienneteEchelonJours % 365) % 30;
		if (annee == 1) {
			res += annee + " an";
		} else if (annee > 1) {
			res += annee + " ans";

		}

		if (mois > 0) {
			if (res.equals(""))
				res += mois + " mois";
			else
				res += ", " + mois + " mois";
		}
		if (jour == 1) {
			if (res.equals(""))
				res += jour + " jour";
			else
				res += ", " + jour + " jour";
		} else if (jour > 1) {
			if (res.equals(""))
				res += jour + " jours";
			else
				res += ", " + jour + " jours";
		}

		return res;
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public EaeListItemDto getEaeCourant() {
		return eaeCourant;
	}

	public void setEaeCourant(EaeListItemDto eaeCourant) {
		this.eaeCourant = eaeCourant;
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
	}

	public EaeIdentificationDto getIdentification() {
		return identification;
	}

	public void setIdentification(EaeIdentificationDto identification) {
		this.identification = identification;
	}

	public boolean isModification() {
		return isModification;
	}

	public void setModification(boolean isModification) {
		this.isModification = isModification;
	}

	public boolean isHasTextChanged() {
		return hasTextChanged;
	}

	public void setHasTextChanged(boolean hasTextChanged) {
		this.hasTextChanged = hasTextChanged;
	}

	public Tabbox getTabboxCourant() {
		return tabboxCourant;
	}

	public void setTabboxCourant(Tabbox tabboxCourant) {
		this.tabboxCourant = tabboxCourant;
	}

	public String getModeSaisi() {
		return modeSaisi;
	}

	public void setModeSaisi(String modeSaisi) {
		this.modeSaisi = modeSaisi;
	}

	public List<EaeFichePosteDto> getListeFichePoste() {
		return listeFichePoste;
	}

	public void setListeFichePoste(List<EaeFichePosteDto> listeFichePoste) {
		this.listeFichePoste = listeFichePoste;
	}

	public EaeFichePosteDto getFichePostePrimaire() {
		return fichePostePrimaire;
	}

	public void setFichePostePrimaire(EaeFichePosteDto fichePostePrimaire) {
		this.fichePostePrimaire = fichePostePrimaire;
	}

	public EaeFichePosteDto getFichePosteSecondaire() {
		return fichePosteSecondaire;
	}

	public void setFichePosteSecondaire(EaeFichePosteDto fichePosteSecondaire) {
		this.fichePosteSecondaire = fichePosteSecondaire;
	}

	public EaeResultatDto getResultat() {
		return resultat;
	}

	public void setResultat(EaeResultatDto resultat) {
		this.resultat = resultat;
	}

	public EaeAppreciationDto getAppreciationAnnee() {
		return appreciationAnnee;
	}

	public void setAppreciationAnnee(EaeAppreciationDto appreciationAnnee) {
		this.appreciationAnnee = appreciationAnnee;
	}

	public EaeAppreciationDto getAppreciationAnneePrec() {
		return appreciationAnneePrec;
	}

	public void setAppreciationAnneePrec(EaeAppreciationDto appreciationAnneePrec) {
		this.appreciationAnneePrec = appreciationAnneePrec;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Integer getAnneePrec() {
		return anneePrec;
	}

	public void setAnneePrec(Integer anneePrec) {
		this.anneePrec = anneePrec;
	}

	public EaeEvaluationDto getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(EaeEvaluationDto evaluation) {
		this.evaluation = evaluation;
	}

	public Date getDureeEntretien() {
		if (dureeEntretien == null && getEvaluation().getDureeEntretien() != null) {
			DateTime t = new DateTime(2014, 01, 01, getEvaluation().getDureeEntretien().getHeures(), getEvaluation()
					.getDureeEntretien().getMinutes());
			return t.toDate();
		}
		return dureeEntretien;
	}

	public Integer toInteger(String value) {
		return Integer.valueOf(value);
	}

	public void setDureeEntretien(Date dureeEntretien) {
		this.dureeEntretien = dureeEntretien;
	}

	public EaeAutoEvaluationDto getAutoEvaluation() {
		return autoEvaluation;
	}

	public void setAutoEvaluation(EaeAutoEvaluationDto autoEvaluation) {
		this.autoEvaluation = autoEvaluation;
	}

	public EaePlanActionDto getPlanAction() {
		return planAction;
	}

	public void setPlanAction(EaePlanActionDto planAction) {
		this.planAction = planAction;
	}

	public EaeEvolutionDto getEvolution() {
		return evolution;
	}

	public void setEvolution(EaeEvolutionDto evolution) {
		this.evolution = evolution;
	}
}
