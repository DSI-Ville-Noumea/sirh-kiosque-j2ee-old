package nc.noumea.mairie.kiosque.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<DemandeDto> listeDemandes;

	private DemandeDto demandeCourant;

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
}
