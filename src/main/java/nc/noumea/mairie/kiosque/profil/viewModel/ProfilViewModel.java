package nc.noumea.mairie.kiosque.profil.viewModel;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.zkoss.bind.annotation.Init;
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

	@Init
	public void initProfilAgent() {
		Session sess = Sessions.getCurrent();
		ProfilAgentDto userDto = (ProfilAgentDto) sess.getAttribute("currentUser");
		ProfilAgentDto result = sirhWsConsumer.getEtatCivil(userDto.getAgent().getIdAgent());
		setAgentCourant(result);
		setSclassPhoto(getAgentCourant().getSexe().equals("M") ? "man" : "woman");
		setShowCouvertureSociale(result.getCouvertureSociale() != null);
		setShowCompte(result.getCompte() != null);
		setShowEnfant(result.getListeEnfants().size() != 0);
		setShowContact(result.getListeContacts().size() != 0);
		setShowAdresse(result.getAdresse() != null);
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
}
