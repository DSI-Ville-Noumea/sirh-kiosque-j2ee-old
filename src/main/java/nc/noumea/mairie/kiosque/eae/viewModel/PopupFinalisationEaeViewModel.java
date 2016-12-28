package nc.noumea.mairie.kiosque.eae.viewModel;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Mairie de Nouméa
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFinalisationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFinalizationInformationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PopupFinalisationEaeViewModel {

	private ProfilAgentDto					currentUser;

	private EaeFinalizationInformationDto	finalisationCourant;

	@WireVariable
	private ISirhEaeWSConsumer				eaeWsConsumer;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("eaeCourant") EaeListItemDto eae) {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupere l'EAE selectionné
		EaeFinalizationInformationDto finalisation = eaeWsConsumer.getFinalisationInformation(eae.getIdEae(), currentUser.getAgent().getIdAgent());
		setFinalisationCourant(finalisation);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String concatAgentWithDto(AgentDto agent) {
		if(agent==null || agent.getNom()==null || agent.getPrenom()==null){
			return null;
		}
		
		return concatAgent(agent.getNom(), agent.getPrenom());
	}

	public String getEvaluateurs(List<AgentDto> evaluateurs) {
		String res = "";
		for (AgentDto eval : evaluateurs) {
			res += concatAgent(eval.getNom(), eval.getPrenom()) + ", ";
		}
		if (!res.equals("")) {
			res = res.substring(0, res.length() - 2);
		}
		return res;
	}

	public EaeFinalizationInformationDto getFinalisationCourant() {
		return finalisationCourant;
	}

	public void setFinalisationCourant(EaeFinalizationInformationDto finalisationCourant) {
		this.finalisationCourant = finalisationCourant;
	}

	@Command
	public void finaliseEae(@BindingParam("win") Window window) {
		ReturnMessageDto result = new ReturnMessageDto();
		// verifier que la note soit comprise entre 0 et 20
		// verifier fichier et note obligatoire
		Double b = getFinalisationCourant().getNoteAnnee();
		if (b == null) {
			result.getErrors().add("La note est obligatoire.");
		} else if ((b < 0 || b > 20)) {
			result.getErrors().add("La note doit etre un nombre compris entre 0 et 20.");
		}
		// verification2 décimales max
		try {
			String decimale = b.toString().substring(b.toString().indexOf(".") + 1, b.toString().length());
			if (decimale.length() > 2) {
				result.getErrors().add("La note doit avoir 2 décimales maximum.");
			}
		} catch (Exception e) {
		}
		// vérifie fichier PDF et obligatoire
		if (getFinalisationCourant().getbFile() == null || getFinalisationCourant().getNameFile() == null) {
			result.getErrors().add("Veuillez sélectionner votre fichier EAE avant de pouvoir finaliser.");
		} else {
			try {
				String nomFichier = getFinalisationCourant().getNameFile();
				String extension = nomFichier.substring(nomFichier.lastIndexOf(".") + 1, nomFichier.length()).toUpperCase();
				if (!extension.equals("PDF")) {
					result.getErrors().add("Le fichier numérisé de votre EAE doit être un fichier au format PDF.");
				}
			} catch (Exception e) {
				result.getErrors().add("Le fichier numérisé de votre EAE doit être un fichier au format PDF.");
			}
		}
		if (result.getErrors().size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			for (String error : result.getErrors()) {
				ValidationMessage vm = new ValidationMessage(error);
				listErreur.add(vm);
			}
			map.put("errors", listErreur);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return;

		}

		EaeFinalisationDto eaeFinalisationDto = new EaeFinalisationDto();
		eaeFinalisationDto.setAnnee(new Integer(getFinalisationCourant().getAnnee()).toString());
		eaeFinalisationDto.setCommentaire(getFinalisationCourant().getCommentaire());
		eaeFinalisationDto.setDateFinalisation(new Date());
		eaeFinalisationDto.setNoteAnnee(b.floatValue());
		eaeFinalisationDto.setTypeFile(getFinalisationCourant().getTypeFile());
		eaeFinalisationDto.setbFile(getFinalisationCourant().getbFile());
		
		result = eaeWsConsumer.finalizeEae(getFinalisationCourant().getIdEae(), currentUser.getAgent().getIdAgent(), eaeFinalisationDto);
		
		if (!result.getErrors().isEmpty() 
				|| !result.getInfos().isEmpty()) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
			List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
			for (String error : result.getErrors()) {
				ValidationMessage vm = new ValidationMessage(error);
				listErreur.add(vm);
			}
			for (String info : result.getInfos()) {
				ValidationMessage vm = new ValidationMessage(info);
				listInfo.add(vm);
			}
			map.put("errors", listErreur);
			map.put("infos", listInfo);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			
			Div divContent = (Div) Path.getComponent("/windowIndex/content");
			
			final HashMap<String, Object> mapChangeEcran = new HashMap<String, Object>();
			mapChangeEcran.put("page", "/eae/tableauEae");
			mapChangeEcran.put("ecran", divContent);
			if (listErreur.size() == 0) {
				BindUtils.postGlobalCommand(null, null, "changeEcran", mapChangeEcran);
				window.detach();
			}
		}
	}

	@Command
	@NotifyChange("finalisationCourant")
	public void onUploadPDF(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if (upEvent != null && null != upEvent.getMedias()) {
			for (Media media : upEvent.getMedias()) {

				getFinalisationCourant().setbFile(media.getByteData());
				getFinalisationCourant().setNameFile(media.getName());
				getFinalisationCourant().setTypeFile(media.getContentType());
			}
		}
	}

}
