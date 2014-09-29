package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AjoutDemandeViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;

	private DemandeDto demandeCreation;

	private List<RefTypeAbsenceDto> listeTypeAbsence;

	private RefTypeAbsenceDto typeAbsenceCourant;

	private String etatDemandeCreation;

	private List<OrganisationSyndicaleDto> listeOrganisationsSyndicale;

	private OrganisationSyndicaleDto organisationsSyndicaleCourant;

	// pour savoir si la date de debut est le matin
	private String selectDebutAM;
	// pour savoir si la date de fin est le matin
	private String selectFinAM;

	@Init
	public void initAjoutDemande() {
		// on vide
		viderZones();
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = absWsConsumer.getServicesAbsences(9003041);
		setListeServicesFiltre(filtreService);
		// pour les agents, on ne rempli pas la liste, elle le sera avec le
		// choix du service
		setListeAgentsFiltre(null);
	}

	@Command
	@NotifyChange({ "listeTypeAbsence" })
	public void chargeFamille() {
		// on recharge les types d'absences
		List<RefTypeAbsenceDto> result = absWsConsumer.getRefTypeAbsenceKiosque(getAgentFiltre().getIdAgent(), null);
		setListeTypeAbsence(result);
	}

	@Command
	@NotifyChange({ "listeOrganisationsSyndicale", "etatDemandeCreation", "demandeCreation" })
	public void chargeFormulaire() {
		// on recharge les oragnisations syndicales
		List<OrganisationSyndicaleDto> orga = absWsConsumer.getListOrganisationSyndicale();
		setListeOrganisationsSyndicale(orga);
		// on positionne la selection du statut Provisoire/Définitif
		setEtatDemandeCreation("0");
		// on initialise la demande
		setDemandeCreation(new DemandeDto());
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void chargeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = absWsConsumer.getAgentsAbsences(9003041, getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	@Command
	public void cancelDemande(@BindingParam("win") Window window) {
		window.detach();
	}

	private void viderZones() {
		setListeAgentsFiltre(null);
		setListeServicesFiltre(null);
		setAgentFiltre(null);
		setServiceFiltre(null);
		setDemandeCreation(null);
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
			agentWithServiceDto.setIdAgent(getAgentFiltre().getIdAgent());

			getDemandeCreation().setIdRefEtat(Integer.valueOf(getEtatDemandeCreation()));
			getDemandeCreation().setIdTypeDemande(getTypeAbsenceCourant().getIdRefTypeAbsence());
			getDemandeCreation().setGroupeAbsence(getTypeAbsenceCourant().getGroupeAbsence());
			getDemandeCreation().setTypeSaisi(getTypeAbsenceCourant().getTypeSaisiDto());
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

			ReturnMessageDto result = absWsConsumer.saveDemandeAbsence(9003041, getDemandeCreation());

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

	private boolean IsFormValid(RefTypeAbsenceDto refTypeAbsenceDto) {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Date de debut
		if (getDemandeCreation().getDateDebut() == null) {
			vList.add(new ValidationMessage("La date de début est obligatoire."));
		}
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
			if (getDemandeCreation().getMotif() == null) {
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
}
