package nc.noumea.mairie.kiosque.profil.viewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProfilViewModel {

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	private ProfilAgentDto agentCourant;

	private String sclassPhoto;

	private boolean showCouvertureSociale;

	private boolean showCompte;

	private boolean showEnfant;

	private boolean showContact;

	private boolean showAdresse;

	private boolean showBP;

	@Init
	public void initProfilAgent() {
		Session sess = Sessions.getCurrent();
		ProfilAgentDto userDto = (ProfilAgentDto) sess.getAttribute("currentUser");
		ProfilAgentDto result = sirhWsConsumer.getEtatCivil(userDto.getAgent().getIdAgent());
		setAgentCourant(result);
		setSclassPhoto(getAgentCourant().getSexe().equals("M") ? "man" : "woman");
		setShowCouvertureSociale(false);
		setShowCompte(false);
		setShowEnfant(false);
		setShowContact(false);
		setShowAdresse(false);
		setShowBP(false);
	}

	@Command
	@NotifyChange({ "showAdresse" })
	public void voirBP() {
		if (isShowBP())
			setShowBP(false);
		else
			setShowBP(true);
	}

	@Command
	@NotifyChange({ "showAdresse" })
	public void voirAdresse() {
		if (isShowAdresse())
			setShowAdresse(false);
		else
			setShowAdresse(true);
	}

	@Command
	@NotifyChange({ "showContact" })
	public void voirContact() {
		if (isShowContact())
			setShowContact(false);
		else
			setShowContact(true);
	}

	@Command
	@NotifyChange({ "showEnfant" })
	public void voirEnfant() {
		if (isShowEnfant())
			setShowEnfant(false);
		else
			setShowEnfant(true);
	}

	@Command
	@NotifyChange({ "showCompte" })
	public void voirCompte() {
		if (isShowCompte())
			setShowCompte(false);
		else
			setShowCompte(true);
	}

	@Command
	@NotifyChange({ "showCouvertureSociale" })
	public void voirCouvertureSociale() {
		if (isShowCouvertureSociale())
			setShowCouvertureSociale(false);
		else
			setShowCouvertureSociale(true);
	}

	public String getDateNaissance(Date dateNaissance) {
		if (dateNaissance == null)
			return "";
		return new SimpleDateFormat("dd/MM/yyyy").format(dateNaissance);
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

	public boolean isShowCompte() {
		return showCompte;
	}

	public void setShowCompte(boolean showCompte) {
		this.showCompte = showCompte;
	}

	public boolean isShowEnfant() {
		return showEnfant;
	}

	public void setShowEnfant(boolean showEnfant) {
		this.showEnfant = showEnfant;
	}

	public boolean isShowContact() {
		return showContact;
	}

	public void setShowContact(boolean showContact) {
		this.showContact = showContact;
	}

	public boolean isShowAdresse() {
		return showAdresse;
	}

	public void setShowAdresse(boolean showAdresse) {
		this.showAdresse = showAdresse;
	}

	public boolean isShowBP() {
		return showBP;
	}

	public void setShowBP(boolean showBP) {
		this.showBP = showBP;
	}
}
