package nc.noumea.mairie.kiosque.viewModel;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
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

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsAbsDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class AbstractViewModel extends SelectorComposer<Component> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5038609806558569441L;

	private Logger logger = LoggerFactory.getLogger(AbstractViewModel.class);
	
	@WireVariable
	protected ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	protected ISirhPtgWSConsumer ptgWsConsumer;

	@WireVariable
	protected ISirhWSConsumer sirhWsConsumer;

	@WireVariable
	protected AccessRightsAbsDto droitsAbsence;

	@WireVariable
	protected AccessRightsPtgDto droitsPointage;

	@WireVariable
	protected ProfilAgentDto currentUser;

	@WireVariable
	protected Boolean droitsEae;
	
	public ProfilAgentDto getCurrentUser() {

		if(null != currentUser) {
			return currentUser;
		}
		
		setCurrentUser((ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser"));
		
		return currentUser;
	}

	public void setCurrentUser(ProfilAgentDto currentUser) {
		this.currentUser = currentUser;
	}

	public AccessRightsAbsDto getDroitsAbsence() {
		
		if(null != droitsAbsence) {
			return droitsAbsence;
		}
		
		droitsAbsence = (AccessRightsAbsDto) Sessions.getCurrent().getAttribute("droitsAbsence");
		if(null != droitsAbsence) {
			setDroitsAbsence(droitsAbsence);
			return droitsAbsence;
		}
		
		/* Pour les absences */
		try {
			AccessRightsAbsDto droitsAbsence = absWsConsumer.getDroitsAbsenceAgent(getCurrentUser().getAgent().getIdAgent());
			setDroitsAbsence(droitsAbsence);
			Sessions.getCurrent().setAttribute("droitsAbsence", droitsAbsence);
		} catch (Exception e) {
			// l'appli SIRH-ABS-WS ne semble pas répondre
			logger.error("L'application SIRH-ABS-WS ne répond pas.");
		}
		
		return droitsAbsence;
	}

	public void setDroitsAbsence(AccessRightsAbsDto droitsAbsence) {
		this.droitsAbsence = droitsAbsence;
	}

	public AccessRightsPtgDto getDroitsPointage() {
		
		if(null != droitsPointage) {
			return droitsPointage;
		}
		
		droitsPointage = (AccessRightsPtgDto) Sessions.getCurrent().getAttribute("droitsPointage");
		if(null != droitsPointage) {
			setDroitsPointage(droitsPointage);
			return droitsPointage;
		}
		
		/* Pour les pointages */
		try {
			AccessRightsPtgDto droitsPointage = ptgWsConsumer.getListAccessRightsByAgent(getCurrentUser().getAgent()
					.getIdAgent());
			setDroitsPointage(droitsPointage);
			Sessions.getCurrent().setAttribute("droitsPointage", droitsPointage);
		} catch (Exception e) {
			// l'appli SIRH-PTG-WS ne semble pas répondre
			logger.error("L'application SIRH-PTG-WS ne répond pas.");
		}
		
		return droitsPointage;
	}

	public void setDroitsPointage(AccessRightsPtgDto droitsPointage) {
		this.droitsPointage = droitsPointage;
	}

	public boolean isDroitsEae() {
		
		if(null != droitsEae) {
			return droitsEae;
		}
		
		droitsEae = (Boolean) Sessions.getCurrent().getAttribute("droitsEae");
		if(null != droitsEae) {
			setDroitsEae(droitsEae);
			return droitsEae;
		}

		/* Pour les eaes */
		try {
			boolean droitsEae = sirhWsConsumer.estHabiliteEAE(getCurrentUser().getAgent().getIdAgent());
			setDroitsEae(droitsEae);
			Sessions.getCurrent().setAttribute("droitsEae", droitsEae);
		} catch (Exception e) {
			// l'appli SIRH-EAE-WS ne semble pas répondre
			logger.error("L'application SIRH-WS ne répond pas.");
		}
		
		return droitsEae;
	}

	public void setDroitsEae(boolean droitsEae) {
		this.droitsEae = droitsEae;
	}
	
}
