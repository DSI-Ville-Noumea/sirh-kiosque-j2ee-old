package nc.noumea.mairie.kiosque.eae.viewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFinalizationInformationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

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
	public void finaliseEae() {
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

		// TODO faire appel à Eae pour alfresco
	}

	@Command
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
			}
		}
	}

}
