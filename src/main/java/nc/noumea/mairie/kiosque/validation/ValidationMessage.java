package nc.noumea.mairie.kiosque.validation;

public class ValidationMessage {
	private String message;

	public ValidationMessage(String message) {
		this.message = message;
	}

	public ValidationMessage() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
