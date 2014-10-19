package nc.noumea.mairie.kiosque.eae.viewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EaeViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private EaeListItemDto eaeCourant;

	private Tab tabCourant;

	private EaeIdentificationDto identification;

	@Init
	public void initTableauBord(@ExecutionArgParam("eae") EaeListItemDto eae) {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		setEaeCourant(eae);
		// on charge l'identification
		EaeIdentificationDto identification = eaeWsConsumer.getIdentificationEae(eae.getIdEae(), currentUser.getAgent()
				.getIdAgent());
		setIdentification(identification);
	}

	@Command
	@NotifyChange({ "eaeCourant" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		// on sauvegarde l'onglet
		setTabCourant(tab);
	}

	@Command
	@NotifyChange({ "eaeCourant" })
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
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
}
