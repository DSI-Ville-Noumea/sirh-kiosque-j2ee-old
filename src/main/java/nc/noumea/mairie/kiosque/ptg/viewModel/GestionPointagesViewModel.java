package nc.noumea.mairie.kiosque.ptg.viewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypePointageDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GestionPointagesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private ProfilAgentDto currentUser;

	private List<ConsultPointageDto> listePointages;

	private ConsultPointageDto pointageCourant;

	/* POUR LES FILTRES */
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private List<RefEtatPointageDto> listeEtatPointageFiltre;
	private RefEtatPointageDto etatPointageFiltre;
	private List<RefTypePointageDto> listeTypePointageFiltre;
	private RefTypePointageDto typePointageFiltre;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initGestionPointages() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		// on recharge les états de pointages pour les filtres
		List<RefEtatPointageDto> filtreEtat = ptgWsConsumer.getEtatPointageKiosque();
		setListeEtatPointageFiltre(filtreEtat);
		// on recharge les types de pointages pour les filtres
		List<RefTypePointageDto> filtreType = ptgWsConsumer.getTypePointageKiosque();
		setListeTypePointageFiltre(filtreType);
		setTailleListe("5");
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void filtrer() {
		if (IsFiltreValid()) {
			List<ConsultPointageDto> result = ptgWsConsumer.getListePointages(currentUser.getAgent().getIdAgent(),
					getDateDebutFiltre(), getDateFinFiltre(), getServiceFiltre().getCodeService(),
					getAgentFiltre() == null ? null : getAgentFiltre().getIdAgent(),
					getEtatPointageFiltre() == null ? null : getEtatPointageFiltre().getIdRefEtat(),
					getTypePointageFiltre() == null ? null : getTypePointageFiltre().getIdRefTypePointage());
			setListePointages(result);
		}
	}

	private boolean IsFiltreValid() {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Date de debut
		if (getDateDebutFiltre() == null) {
			vList.add(new ValidationMessage("La date de début est obligatoire."));
		}
		// Date de fin
		if (getDateFinFiltre() == null) {
			vList.add(new ValidationMessage("La date de fin est obligatoire."));
		}
		// Service
		if (getServiceFiltre() == null) {
			vList.add(new ValidationMessage("Le service est obligatoire."));
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}

	@Command
	@NotifyChange({ "serviceFiltre", "dateDebutFiltre", "dateFinFiltre", "etatPointageFiltre", "typePointageFiltre",
			"listeAgentsFiltre", "agentFiltre" })
	public void viderFiltre() {
		setServiceFiltre(null);
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setEtatPointageFiltre(null);
		setTypePointageFiltre(null);
		setListeAgentsFiltre(null);
		setAgentFiltre(null);
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void afficheListeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String concatAgentNomatr(AgentDto ag) {
		String nomatr = ag.getIdAgent().toString().substring(3, ag.getIdAgent().toString().length());
		return ag.getNom() + " " + ag.getPrenom() + " (" + nomatr + ")";
	}

	@Command
	public void exportPDF(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfExporter exporter = new PdfExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("gestionPointages.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	public void exportExcel(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("gestionPointages.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getTailleListe() {
		return tailleListe;
	}

	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
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

	public Date getDateDebutFiltre() {
		return dateDebutFiltre;
	}

	public void setDateDebutFiltre(Date dateDebutFiltre) {
		this.dateDebutFiltre = dateDebutFiltre;
	}

	public Date getDateFinFiltre() {
		return dateFinFiltre;
	}

	public void setDateFinFiltre(Date dateFinFiltre) {
		this.dateFinFiltre = dateFinFiltre;
	}

	public List<RefEtatPointageDto> getListeEtatPointageFiltre() {
		return listeEtatPointageFiltre;
	}

	public void setListeEtatPointageFiltre(List<RefEtatPointageDto> listeEtatPointageFiltre) {
		this.listeEtatPointageFiltre = listeEtatPointageFiltre;
	}

	public RefEtatPointageDto getEtatPointageFiltre() {
		return etatPointageFiltre;
	}

	public void setEtatPointageFiltre(RefEtatPointageDto etatPointageFiltre) {
		this.etatPointageFiltre = etatPointageFiltre;
	}

	public List<RefTypePointageDto> getListeTypePointageFiltre() {
		return listeTypePointageFiltre;
	}

	public void setListeTypePointageFiltre(List<RefTypePointageDto> listeTypePointageFiltre) {
		this.listeTypePointageFiltre = listeTypePointageFiltre;
	}

	public RefTypePointageDto getTypePointageFiltre() {
		return typePointageFiltre;
	}

	public void setTypePointageFiltre(RefTypePointageDto typePointageFiltre) {
		this.typePointageFiltre = typePointageFiltre;
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

	public List<ConsultPointageDto> getListePointages() {
		return listePointages;
	}

	public void setListePointages(List<ConsultPointageDto> listePointages) {
		this.listePointages = listePointages;
	}

	public ConsultPointageDto getPointageCourant() {
		return pointageCourant;
	}

	public void setPointageCourant(ConsultPointageDto pointageCourant) {
		this.pointageCourant = pointageCourant;
	}
}
