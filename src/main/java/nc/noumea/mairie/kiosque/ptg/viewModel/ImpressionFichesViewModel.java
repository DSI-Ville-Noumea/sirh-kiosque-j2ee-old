package nc.noumea.mairie.kiosque.ptg.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImpressionFichesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private ProfilAgentDto currentUser;

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
}
