package nc.noumea.mairie.kiosque.eae.viewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
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

	/* Pour savoir si on est en modif ou en visu */
	private String modeSaisi;
	private boolean isModification;
	/* Pour savoir si on affiche la disquette de sauvegarde */
	private boolean hasTextChanged;

	@Init
	public void initEae(@ExecutionArgParam("eae") EaeListItemDto eae, @ExecutionArgParam("mode") String modeSaisi) {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		setModeSaisi(modeSaisi);
		setEaeCourant(eae);

		setModification(modeSaisi.equals("EDIT"));
		// on charge l'identification
		EaeIdentificationDto identification = eaeWsConsumer.getIdentificationEae(getEaeCourant().getIdEae(),
				currentUser.getAgent().getIdAgent());
		setIdentification(identification);
	}

	@GlobalCommand
	@NotifyChange({ "hasTextChanged", "identification" })
	public void engistreOnglet() {
		// on sauvegarde l'onglet
		ReturnMessageDto result = new ReturnMessageDto();
		if (getTabCourant().getId().equals("IDENTIFICATION")) {
			result = eaeWsConsumer.saveIdentification(getIdentification().getIdEae(), currentUser.getAgent()
					.getIdAgent(), getIdentification());
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

	@GlobalCommand
	@NotifyChange({ "hasTextChanged", "identification" })
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
}
