package nc.noumea.mairie.kiosque.viewModel;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private AccessRightsDto droitsAbsence;

	@Init
	public void initMenu() {
		AccessRightsDto droitsAbsence = absWsConsumer.getDroitsAbsenceAgent(9005138);
		//TODO
		//A supprimer, c'est pour l'affichage du menu pour la démo
		droitsAbsence.setMajSolde(true);
		//fin TODO
		setDroitsAbsence(droitsAbsence);
	}

	@Command
	public void changeEcran(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Executions.createComponents(page + ".zul", div, null);

	}

	public AccessRightsDto getDroitsAbsence() {
		return droitsAbsence;
	}

	public void setDroitsAbsence(AccessRightsDto droitsAbsence) {
		this.droitsAbsence = droitsAbsence;
	}
}
