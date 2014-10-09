package nc.noumea.mairie.kiosque.ptg.viewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImpressionFichesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private ProfilAgentDto currentUser;

	private List<AgentDto> listeAgents;

	@SuppressWarnings("unused")
	private boolean isChecked;

	private List<String> listeIdAgentsToPrint;
	private Date dateLundi;

	/* POUR LES FILTRES */
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initImpressionFiches() {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		setTailleListe("5");
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void afficheListeFiche() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getFichesToPrint(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgents(filtreAgent);
	}

	@Command
	public void checkImpressionFiche(@BindingParam("agent") Integer idAgent, @BindingParam("checkbox") Checkbox checkbox) {
		List<String> list = new ArrayList<>();
		if (getListeIdAgentsToPrint() != null)
			list.addAll(getListeIdAgentsToPrint());
		if (checkbox.isChecked()) {
			list.add(idAgent.toString());
		} else {
			list.remove(idAgent.toString());
		}
		setListeIdAgentsToPrint(list);
	}

	@Command
	@NotifyChange({ "listeAgents" })
	public void checkAllImpressionFiche(@BindingParam("checkbox") Checkbox checkbox) {
		if (checkbox.isChecked()) {
			List<String> list = new ArrayList<>();
			for (AgentDto dto : getListeAgents()) {
				list.add(dto.getIdAgent().toString());
			}
			setListeIdAgentsToPrint(list);
		} else {
			setListeIdAgentsToPrint(null);
		}
	}

	@Command
	public void imprimerFiches() {
		// on imprime la demande
		byte[] resp = ptgWsConsumer.imprimerFiches(currentUser.getAgent().getIdAgent(), getDateLundi(),
				getListeIdAgentsToPrint());
		Filedownload.save(resp, "application/pdf", "fichesPointage");
	}

	public boolean checked(Integer idAgent) {
		if (getListeIdAgentsToPrint() == null)
			return false;
		return getListeIdAgentsToPrint().contains(idAgent.toString());
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String nomatrAgent(Integer idAgent) {
		String idAgentConvert = idAgent.toString();
		return idAgentConvert.substring(3, idAgentConvert.length());
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

	public List<AgentDto> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<AgentDto> listeAgents) {
		this.listeAgents = listeAgents;
	}

	public List<String> getListeIdAgentsToPrint() {
		return listeIdAgentsToPrint;
	}

	public void setListeIdAgentsToPrint(List<String> listeIdAgentsToPrint) {
		this.listeIdAgentsToPrint = listeIdAgentsToPrint;
	}

	public Date getDateLundi() {
		return dateLundi;
	}

	public void setDateLundi(Date dateLundi) {
		this.dateLundi = dateLundi;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
