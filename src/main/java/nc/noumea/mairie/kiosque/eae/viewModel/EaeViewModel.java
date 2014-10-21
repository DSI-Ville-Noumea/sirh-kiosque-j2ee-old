package nc.noumea.mairie.kiosque.eae.viewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAppreciationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeObjectifDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeResultatDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

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

	private Integer annee;

	private Integer anneePrec;

	private EaeAppreciationDto appreciationAnneePrec;

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
	@NotifyChange({ "hasTextChanged", "identification", "resultat", "appreciationAnnee", "appreciationAnneePrec" })
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
	@NotifyChange({ "hasTextChanged", "identification", "resultat", "appreciationAnnee", "appreciationAnneePrec" })
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
}
