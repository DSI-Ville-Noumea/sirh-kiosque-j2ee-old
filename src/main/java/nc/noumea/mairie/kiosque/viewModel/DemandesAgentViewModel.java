package nc.noumea.mairie.kiosque.viewModel;

import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesAgentViewModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

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
	@NotifyChange({ "listeDemandes" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		List<DemandeDto> result = absWsConsumer.getDemandesAgent(9005138, tab.getId());
		setListeDemandes(result);
	}

	@Listen("onClick = #AJOUT")
	public void showModal(Event e) {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("ajoutDemandeAgent.zul", null, null);
		win.doModal();
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
