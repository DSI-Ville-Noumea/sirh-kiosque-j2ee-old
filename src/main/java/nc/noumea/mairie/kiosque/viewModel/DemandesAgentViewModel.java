package nc.noumea.mairie.kiosque.viewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<DemandeDto> listeDemandes;

	private DemandeDto demandeCourant;

	private DemandeDto demandeCreation;

	private List<RefTypeAbsenceDto> listeTypeAbsence;

	private RefTypeAbsenceDto typeAbsenceCourant;

	private String etatDemandeCreation;

	private List<OrganisationSyndicaleDto> listeOrganisationsSyndicale;

	private OrganisationSyndicaleDto organisationsSyndicaleCourant;

	@Init
	public void initDemandes() {
		List<DemandeDto> result = absWsConsumer.getDemandesAgent(9005138, "NON_PRISES");
		setListeDemandes(result);
	}

	@Command
	@NotifyChange("listeDemandes")
	public void changeVue(@BindingParam("tab") Tab tab) {
		List<DemandeDto> result = absWsConsumer.getDemandesAgent(9005138, tab.getId());
		setListeDemandes(result);

	}

	@Command
	@NotifyChange({ "listeTypeAbsence", "demandeCreation", "etatDemandeCreation", "listeOrganisationsSyndicale" })
	public void creerDemande() {
		List<RefTypeAbsenceDto> result = absWsConsumer.getRefTypeAbsenceKiosque(9005138);
		setListeTypeAbsence(result);
		List<OrganisationSyndicaleDto> orga = absWsConsumer.getListOrganisationSyndicale();
		setListeOrganisationsSyndicale(orga);
		setDemandeCreation(new DemandeDto());
		setEtatDemandeCreation("0");

	}

	@Command
	@NotifyChange({ "typeAbsenceCourant", "organisationsSyndicaleCourant" })
	public void cancelDemande() {
		setTypeAbsenceCourant(null);
		setOrganisationsSyndicaleCourant(null);
	}

	@Command
	public void saveDemande() {
		if (IsFormValid()) {
			AgentWithServiceDto agentWithServiceDto = new AgentWithServiceDto();
			agentWithServiceDto.setIdAgent(9005138);

			getDemandeCreation().setIdRefEtat(Integer.valueOf(getEtatDemandeCreation()));
			getDemandeCreation().setIdTypeDemande(getTypeAbsenceCourant().getIdRefTypeAbsence());
			getDemandeCreation().setGroupeAbsence(getTypeAbsenceCourant().getGroupeAbsence());
			getDemandeCreation().setTypeSaisi(getTypeAbsenceCourant().getTypeSaisiDto());
			getDemandeCreation().setAgentWithServiceDto(agentWithServiceDto);
			getDemandeCreation().setOrganisationSyndicale(getOrganisationsSyndicaleCourant());

			ReturnMessageDto result = absWsConsumer.saveDemandeAbsence(9005138, getDemandeCreation());

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
				Executions.createComponents("../messages/returnMessage.zul", null, map);
			}
		}
	}

	private boolean IsFormValid() {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		if (getDemandeCreation().getDateDebut() == null) {
			vList.add(new ValidationMessage("La date de début ne doit pas être vide."));
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("../messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}

	public List<DemandeDto> getListeDemandes() {
		return listeDemandes;
	}

	public void setListeDemandes(List<DemandeDto> listeDemandes) {
		this.listeDemandes = listeDemandes;
	}

	public DemandeDto getDemandeCourant() {
		return demandeCourant;
	}

	public void setDemandeCourant(DemandeDto demandeCourant) {
		this.demandeCourant = demandeCourant;
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
}
