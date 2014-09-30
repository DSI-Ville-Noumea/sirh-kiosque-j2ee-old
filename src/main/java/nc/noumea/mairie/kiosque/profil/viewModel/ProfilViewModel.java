package nc.noumea.mairie.kiosque.profil.viewModel;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProfilViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private ProfilAgentDto agentCourant;

	private String sclassPhoto;
	
	private boolean showCouvertureSociale;

	@Init
	public void initProfilAgent() {
		Session sess = Sessions.getCurrent();
		ProfilAgentDto userDto = (ProfilAgentDto) sess.getAttribute("currentUser");
		ProfilAgentDto result = sirhWsConsumer.getEtatCivil(userDto.getAgent().getIdAgent());
		setAgentCourant(result);
		setSclassPhoto(getAgentCourant().getSexe().equals("M") ? "man" : "woman");
		setShowCouvertureSociale(false);
	}

	@Command
	public void voirContact() {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("/profil/contact.zul", null, null);
		win.doModal();
	}

	@Command
	public void voirEnfant() {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("/profil/enfant.zul", null, null);
		win.doModal();
	}

	@Command
	public void voirCompte() {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("/profil/compteBancaire.zul", null, null);
		win.doModal();
	}

	@Command
	@NotifyChange({ "showCouvertureSociale" })
	public void voirCouvertureSociale() {
		if (isShowCouvertureSociale())
			setShowCouvertureSociale(false);
		else
			setShowCouvertureSociale(true);
	}

	public ProfilAgentDto getAgentCourant() {
		return agentCourant;
	}

	public void setAgentCourant(ProfilAgentDto agentCourant) {
		this.agentCourant = agentCourant;
	}

	public String getSclassPhoto() {
		return sclassPhoto;
	}

	public void setSclassPhoto(String sclassPhoto) {
		this.sclassPhoto = sclassPhoto;
	}

	public boolean isShowCouvertureSociale() {
		return showCouvertureSociale;
	}

	public void setShowCouvertureSociale(boolean showCouvertureSociale) {
		this.showCouvertureSociale = showCouvertureSociale;
	}
}
