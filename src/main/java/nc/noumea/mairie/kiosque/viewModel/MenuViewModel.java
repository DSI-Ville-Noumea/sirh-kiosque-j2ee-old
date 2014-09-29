package nc.noumea.mairie.kiosque.viewModel;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

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

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private AccessRightsDto droitsAbsence;

	private boolean droitsEae;

	@Init
	public void initMenu() {
		/* Pour les absences */
		AccessRightsDto droitsAbsence = absWsConsumer.getDroitsAbsenceAgent(9003041);
		setDroitsAbsence(droitsAbsence);
		/* Pour les absences */
		boolean droitsEAe = sirhWsConsumer.estHabiliteEAE(9005138);
		// TODO à supprimer
		droitsEAe = true;
		setDroitsEae(droitsEAe);
	}

	@Command
	public void changeEcran(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Executions.createComponents(page + ".zul", div, null);
	}

	@Command
	public void eaeSharepoint(@BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Executions.getCurrent().sendRedirect("http://svq-sp/kiosque-rh/_layouts/Noumea.RH.Eae/EAEList.aspx", "_blank");
	}

	@Command
	public void tableauBordSharepoint(@BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Executions.getCurrent().sendRedirect("http://svq-sp/kiosque-rh/_layouts/Noumea.RH.Eae/EAETableauDeBord.aspx",
				"_blank");
	}

	public AccessRightsDto getDroitsAbsence() {
		return droitsAbsence;
	}

	public void setDroitsAbsence(AccessRightsDto droitsAbsence) {
		this.droitsAbsence = droitsAbsence;
	}

	public boolean isDroitsEae() {
		return droitsEae;
	}

	public void setDroitsEae(boolean droitsEae) {
		this.droitsEae = droitsEae;
	}
}
