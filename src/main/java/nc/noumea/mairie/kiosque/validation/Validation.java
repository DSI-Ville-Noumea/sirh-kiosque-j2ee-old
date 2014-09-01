package nc.noumea.mairie.kiosque.validation;

import java.util.List;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

public class Validation {
	private List<ValidationMessage> errorsMessages = null;
	private List<ValidationMessage> infosMessages = null;

	@Init
	public void init(@ExecutionArgParam("errors") List<ValidationMessage> errors,
			@ExecutionArgParam("infos") List<ValidationMessage> infos) {
		setErrorsMessages(errors);
		setInfosMessages(infos);
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
}
