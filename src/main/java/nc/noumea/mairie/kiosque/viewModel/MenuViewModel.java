package nc.noumea.mairie.kiosque.viewModel;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsDto;
import nc.noumea.mairie.kiosque.cmis.ISharepointService;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	@WireVariable
	private ISharepointService sharepointConsumer;

	private AccessRightsDto droitsAbsence;

	private boolean droitsEae;

	private ProfilAgentDto currentUser;

	@Init
	public void initMenu() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		AccessRightsDto droitsAbsence = absWsConsumer.getDroitsAbsenceAgent(currentUser.getAgent().getIdAgent());
		setDroitsAbsence(droitsAbsence);
		/* Pour les absences */
		boolean droitsEAe = sirhWsConsumer.estHabiliteEAE(currentUser.getAgent().getIdAgent());
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
		Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlEaeApprobateur(), "_blank");
	}

	@Command
	public void tableauBordSharepoint(@BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlTableauBordApprobateur(), "_blank");
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
