package nc.noumea.mairie.kiosque.viewModel;

import java.util.HashMap;
import java.util.Map;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsAbsDto;
import nc.noumea.mairie.kiosque.cmis.ISharepointService;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger logger = LoggerFactory.getLogger(MenuViewModel.class);

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	@WireVariable
	private ISharepointService sharepointConsumer;

	private AccessRightsAbsDto droitsAbsence;

	private AccessRightsPtgDto droitsPointage;

	private boolean droitsModulePointage;

	private boolean droitsEae;

	private ProfilAgentDto currentUser;

	@Init
	public void initMenu() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		/* Pour les absences */
		try {
			AccessRightsAbsDto droitsAbsence = absWsConsumer.getDroitsAbsenceAgent(currentUser.getAgent().getIdAgent());
			setDroitsAbsence(droitsAbsence);
		} catch (Exception e) {
			// l'appli SIRH-ABS-WS ne semble pas répondre
			logger.error("L'application SIRH-ABS-WS ne répond pas.");
		}
		/* Pour les eaes */
		try {
			boolean droitsEAe = sirhWsConsumer.estHabiliteEAE(currentUser.getAgent().getIdAgent());
			setDroitsEae(droitsEAe);
		} catch (Exception e) {
			// l'appli SIRH-EAE-WS ne semble pas répondre
			logger.error("L'application SIRH-EAE-WS ne répond pas.");
		}
		/* Pour les pointages */
		try {
			AccessRightsPtgDto droitsPointage = ptgWsConsumer.getListAccessRightsByAgent(currentUser.getAgent()
					.getIdAgent());
			setDroitsPointage(droitsPointage);
			setDroitsModulePointage(getDroitsPointage().isApprobation() || getDroitsPointage().isFiches()
					|| getDroitsPointage().isGestionDroitsAcces() || getDroitsPointage().isSaisie()
					|| getDroitsPointage().isVisualisation());
		} catch (Exception e) {
			// l'appli SIRH-PTG-WS ne semble pas répondre
			logger.error("L'application SIRH-PTG-WS ne répond pas.");
		}
	}

	@Command
	public void changeEcran(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Executions.createComponents(page + ".zul", div, null);
	}

	@Command
	public void eaeSharepoint(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		if (currentUser.getAgent().getIdAgent() == 9005138) {
			Map<String, Div> args = new HashMap<String, Div>();
			args.put("div", div);

			div.getChildren().clear();
			Executions.createComponents(page + ".zul", div, args);
		} else {
			div.getChildren().clear();
			Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlEaeApprobateur(), "_blank");
		}
	}

	@Command
	public void tableauBordSharepoint(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		if (currentUser.getAgent().getIdAgent() == 9005138) {
			div.getChildren().clear();
			Executions.createComponents(page + ".zul", div, null);
		} else {
			div.getChildren().clear();
			Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlTableauBordApprobateur(), "_blank");
		}
	}

	public AccessRightsAbsDto getDroitsAbsence() {
		return droitsAbsence;
	}

	public void setDroitsAbsence(AccessRightsAbsDto droitsAbsence) {
		this.droitsAbsence = droitsAbsence;
	}

	public boolean isDroitsEae() {
		return droitsEae;
	}

	public void setDroitsEae(boolean droitsEae) {
		this.droitsEae = droitsEae;
	}

	public AccessRightsPtgDto getDroitsPointage() {
		return droitsPointage;
	}

	public void setDroitsPointage(AccessRightsPtgDto droitsPointage) {
		this.droitsPointage = droitsPointage;
	}

	public boolean isDroitsModulePointage() {
		return droitsModulePointage;
	}

	public void setDroitsModulePointage(boolean droitsModulePointage) {
		this.droitsModulePointage = droitsModulePointage;
	}
}
