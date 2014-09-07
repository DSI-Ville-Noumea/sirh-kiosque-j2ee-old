package nc.noumea.mairie.kiosque.viewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeSaisiDto;
import nc.noumea.mairie.kiosque.abs.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ModifierDemandesAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private DemandeDto demandeCourant;

	private List<OrganisationSyndicaleDto> listeOrganisationsSyndicale;

	private OrganisationSyndicaleDto organisationsSyndicaleCourant;

	// pour savoir si la date de debut est le matin
	private String selectDebutAM;
	// pour savoir si la date de fin est le matin
	private String selectFinAM;
	private Double dureeDemande;
	private String etatDemande;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("demandeCourant") DemandeDto demande) {
		// on recupere la demande selectionnée
		setDemandeCourant(demande);

		// on recharge les oragnisations syndicales
		List<OrganisationSyndicaleDto> orga = absWsConsumer.getListOrganisationSyndicale();
		setListeOrganisationsSyndicale(orga);
		setOrganisationsSyndicaleCourant(getDemandeCourant().getOrganisationSyndicale());

		// date AM/PM
		setSelectDebutAM(getDemandeCourant().isDateDebutAM() ? "AM" : "PM");
		setSelectFinAM(getDemandeCourant().isDateFinAM() ? "AM" : "PM");

		// durée
		setDureeDemande(getDureeHeureMinutes(getDemandeCourant().getDuree(), getDemandeCourant().getTypeSaisi()
				.getUniteDecompte()));
		// etat
		setEtatDemande(getDemandeCourant().getIdRefEtat().toString());
	}

	private Double getDureeHeureMinutes(Double duree, String uniteDecompte) {
		if (uniteDecompte.equals("minutes")) {
			return duree / 60;
		}
		return (double) 0;
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	@Command
	public void saveDemande(@BindingParam("win") Window window) {

		if (IsFormValid(getDemandeCourant().getTypeSaisi())) {

			getDemandeCourant().setIdRefEtat(Integer.valueOf(getEtatDemande()));
			getDemandeCourant().setDuree(getDureeDemande() == null ? null : getDureeDemande() * 60);
			getDemandeCourant().setOrganisationSyndicale(getOrganisationsSyndicaleCourant());
			getDemandeCourant().setDateDebutAM(
					getSelectDebutAM() == null ? false : getSelectDebutAM().equals("AM") ? true : false);
			getDemandeCourant().setDateDebutPM(
					getSelectDebutAM() == null ? false : getSelectDebutAM().equals("PM") ? true : false);
			getDemandeCourant().setDateFinAM(
					getSelectFinAM() == null ? false : getSelectFinAM().equals("AM") ? true : false);
			getDemandeCourant().setDateFinPM(
					getSelectFinAM() == null ? false : getSelectFinAM().equals("PM") ? true : false);

			ReturnMessageDto result = absWsConsumer.saveDemandeAbsence(9005138, getDemandeCourant());

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

	private boolean IsFormValid(RefTypeSaisiDto typeSaisie) {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Date de debut
		if (getDemandeCourant().getDateDebut() == null) {
			vList.add(new ValidationMessage("La date de début est obligatoire."));
		}
		if (typeSaisie.isChkDateDebut()) {
			if (getSelectDebutAM() == null) {
				vList.add(new ValidationMessage("Merci de choisir M/AM pour la date de début."));
			}
		}

		// OS
		if (typeSaisie.isCompteurCollectif()) {
			if (getOrganisationsSyndicaleCourant() == null) {
				vList.add(new ValidationMessage("L'organisation syndicale est obligatoire."));
			}
		}

		// DUREE
		if (typeSaisie.isDuree()) {
			if (getDureeDemande() == null || getDureeDemande() == 0) {
				vList.add(new ValidationMessage("La durée est obligatoire."));
			}
		}

		// DATE FIN
		if (typeSaisie.isCalendarDateFin()) {
			if (getDemandeCourant().getDateFin() == null) {
				vList.add(new ValidationMessage("La date de fin est obligatoire."));
			}
		}
		if (typeSaisie.isChkDateFin()) {
			if (getSelectFinAM() == null) {
				vList.add(new ValidationMessage("Merci de choisir M/AM pour la date de fin."));
			}
		}

		// MOTIF
		if (typeSaisie.isMotif()) {
			if (getDemandeCourant().getMotif() == null) {
				vList.add(new ValidationMessage("Le motif est obligatoire."));
			}
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}

	public DemandeDto getDemandeCourant() {
		return demandeCourant;
	}

	public void setDemandeCourant(DemandeDto demandeCourant) {
		this.demandeCourant = demandeCourant;
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

	public Double getDureeDemande() {
		return dureeDemande;
	}

	public void setDureeDemande(Double dureeDemande) {
		this.dureeDemande = dureeDemande;
	}

	public String getEtatDemande() {
		return etatDemande;
	}

	public void setEtatDemande(String etatDemande) {
		this.etatDemande = etatDemande;
	}
}
