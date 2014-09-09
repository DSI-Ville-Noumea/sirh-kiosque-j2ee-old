package nc.noumea.mairie.kiosque.profil.dto;

public class ContactAgentDto {

	private String diffusable;
	private String prioritaire;
	private String typeContact;
	private String description;

	public String getDiffusable() {
		return diffusable;
	}

	public void setDiffusable(String diffusable) {
		this.diffusable = diffusable;
	}

	public String getPrioritaire() {
		return prioritaire;
	}

	public void setPrioritaire(String prioritaire) {
		this.prioritaire = prioritaire;
	}

	public String getTypeContact() {
		return typeContact;
	}

	public void setTypeContact(String typeContact) {
		this.typeContact = typeContact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
