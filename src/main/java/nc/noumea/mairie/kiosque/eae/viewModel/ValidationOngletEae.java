package nc.noumea.mairie.kiosque.eae.viewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.validation.ValidationMessage;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

public class ValidationOngletEae {
	private List<ValidationMessage> errorsMessages = null;
	private List<ValidationMessage> infosMessages = null;
	private Tab tabChoisi;

	@Init
	public void init(@ExecutionArgParam("errors") List<ValidationMessage> errors,
			@ExecutionArgParam("infos") List<ValidationMessage> infos, @ExecutionArgParam("tab") Tab tab) {
		setErrorsMessages(errors);
		setInfosMessages(infos);
		setTabChoisi(tab);
	}

	@Command
	public void engistreOnglet(@BindingParam("win") Window window) {
		BindUtils.postGlobalCommand(null, null, "engistreOnglet", null);
		window.detach();
	}

	@Command
	public void annulerEngistreOnglet(@BindingParam("win") Window window) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("tab", getTabChoisi());
		BindUtils.postGlobalCommand(null, null, "annulerEngistreOnglet", args);
		window.detach();
	}

	public List<ValidationMessage> getErrorsMessages() {
		return errorsMessages;
	}

	public void setErrorsMessages(List<ValidationMessage> errorsMessages) {
		this.errorsMessages = errorsMessages;
	}

	public List<ValidationMessage> getInfosMessages() {
		return infosMessages;
	}

	public void setInfosMessages(List<ValidationMessage> infosMessages) {
		this.infosMessages = infosMessages;
	}

	public Tab getTabChoisi() {
		return tabChoisi;
	}

	public void setTabChoisi(Tab tabChoisi) {
		this.tabChoisi = tabChoisi;
	}
}
