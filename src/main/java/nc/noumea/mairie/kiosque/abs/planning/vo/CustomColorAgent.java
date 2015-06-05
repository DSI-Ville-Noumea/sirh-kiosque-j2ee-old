package nc.noumea.mairie.kiosque.abs.planning.vo;

public class CustomColorAgent {
	
	private Integer idAgent;
	private String color;
	
	public CustomColorAgent(Integer idAgent, String color) {
		this.idAgent = idAgent;
		this.color = color;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
