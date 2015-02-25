package nc.noumea.mairie.kiosque.ptg.viewModel;

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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.PointagesEtatChangeDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.ServiceDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GestionPointagesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private ProfilAgentDto currentUser;

	private List<ConsultPointageDto> listePointages;

	private ConsultPointageDto pointageCourant;

	private AccessRightsPtgDto droitsPointage;

	/* POUR LES FILTRES */
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private List<String> listeTypeHSupFiltre;
	private String typeHSupFiltre;
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private List<RefEtatPointageDto> listeEtatPointageFiltre;
	private RefEtatPointageDto etatPointageFiltre;
	private List<RefTypePointageDto> listeTypePointageFiltre;
	private RefTypePointageDto typePointageFiltre;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private Div divDepart;

	@Init
	public void initGestionPointages(@ExecutionArgParam("div") Div div) {
		setDivDepart(div);
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		/* Pour les pointages */
		AccessRightsPtgDto droitsPointage = ptgWsConsumer.getListAccessRightsByAgent(currentUser.getAgent()
				.getIdAgent());
		setDroitsPointage(droitsPointage);
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		// on recharge les états de pointages pour les filtres
		List<RefEtatPointageDto> filtreEtat = ptgWsConsumer.getEtatPointageKiosque();
		setListeEtatPointageFiltre(filtreEtat);
		// on recharge les types de pointages pour les filtres
		List<RefTypePointageDto> filtreType = ptgWsConsumer.getTypePointageKiosque();
		setListeTypePointageFiltre(filtreType);
		// on recharge les types d'heures sup pour les filtres
		ArrayList<String> listeTypeHS = new ArrayList<String>();
		listeTypeHS.add("Payées");
		listeTypeHS.add("Récupérées");
		listeTypeHS.add("Rappel en service");
		setListeTypeHSupFiltre(listeTypeHS);
		// on initialise la taille du tableau
		setTailleListe("10");
	}

	public List<ConsultPointageDto> getHistoriquePointage(ConsultPointageDto ptg) {
		List<ConsultPointageDto> result = ptgWsConsumer.getHistoriquePointage(currentUser.getAgent().getIdAgent(),
				ptg.getIdPointage());
		return result;
	}

	@Command
	@NotifyChange({ "listePointages" })
	public void filtrer() {
		if (IsFiltreValid()) {
			List<ConsultPointageDto> result = ptgWsConsumer.getListePointages(currentUser.getAgent().getIdAgent(),
					getDateDebutFiltre(), getDateFinFiltre(), getServiceFiltre().getCodeService(),
					getAgentFiltre() == null ? null : getAgentFiltre().getIdAgent(),
					getEtatPointageFiltre() == null ? null : getEtatPointageFiltre().getIdRefEtat(),
					getTypePointageFiltre() == null ? null : getTypePointageFiltre().getIdRefTypePointage(),
					getTypeHSupFiltre());
			setListePointages(result);
		}
	}

	private boolean IsFiltreValid() {

		List<ValidationMessage> vList = new ArrayList<ValidationMessage>();

		// Date de debut
		if (getDateDebutFiltre() == null) {
			vList.add(new ValidationMessage("La date de début est obligatoire."));
		}
		// Date de fin
		if (getDateFinFiltre() == null) {
			vList.add(new ValidationMessage("La date de fin est obligatoire."));
		}
		// Service
		if (getServiceFiltre() == null) {
			vList.add(new ValidationMessage("Le service est obligatoire."));
		}

		if (vList.size() > 0) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("errors", vList);
			Executions.createComponents("/messages/returnMessage.zul", null, map);
			return false;
		} else
			return true;
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void afficheListeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	public String dateSaisieToString(Date dateSaisie) {
		SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
		return sdfJour.format(dateSaisie) + " à " + sdfHeure.format(dateSaisie);
	}

	public String dateToString(Date date) {
		if (date != null) {
			SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
			return sdfJour.format(date);
		}
		return null;

	}

	public String heureToString(Date date) {
		if (date != null) {
			SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
			return sdfHeure.format(date);
		}
		return null;
	}

	public String etatToString(Integer idRefEtat) {
		return EtatPointageEnum.getEtatPointageEnum(idRefEtat).getLibEtat();
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String typeHSup(ConsultPointageDto dto) {
		return dto.isHeuresSupRappelEnService() ? "RS" : dto.isHeuresSupRecuperees() ? "R" : null;
	}

	public String concatAgentNomatr(AgentDto ag) {
		String nomatr = ag.getIdAgent().toString().substring(3, ag.getIdAgent().toString().length());
		return ag.getNom() + " " + ag.getPrenom() + " (" + nomatr + ")";
	}

	@Command
	public void exportPDF(@BindingParam("ref") Grid grid) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfExporter exporter = new PdfExporter();
		exporter.export(grid, out);

		AMedia amedia = new AMedia("gestionPointages.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	public void exportExcel(@BindingParam("ref") Grid grid) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(grid, out);

		AMedia amedia = new AMedia("gestionPointages.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	private void doChangeEtat(List<ConsultPointageDto> listePointages, EtatPointageEnum etatPointage) {
		List<PointagesEtatChangeDto> listeChangeEtat = new ArrayList<>();
		for (ConsultPointageDto ptg : listePointages) {
			PointagesEtatChangeDto dto = new PointagesEtatChangeDto();
			dto.setIdPointage(ptg.getIdPointage());
			dto.setIdRefEtat(etatPointage.getCodeEtat());
			listeChangeEtat.add(dto);
		}
		sauvegardeEtatPointage(listeChangeEtat);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void approuverAllPointage() {
		if (getListePointages() == null || getListePointages().size() == 0) {
			String message = "Il n'y a aucun pointage à accepter.";
			Messagebox.show(message, "Erreur", Messagebox.OK, "");
		} else if (getListePointages().size() == 1) {
			doChangeEtat(getListePointages(), EtatPointageEnum.APPROUVE);
			filtrer();
			BindUtils.postNotifyChange(null, null, GestionPointagesViewModel.this, "listePointages");
		} else {
			// on ouvre une popup de confirmation
			Messagebox.show("Voulez-vous accepter les " + getListePointages().size() + " pointage(s) ?",
					"Confirmation", Messagebox.CANCEL | Messagebox.OK, "", new EventListener() {
						@Override
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								doChangeEtat(getListePointages(), EtatPointageEnum.APPROUVE);
								filtrer();
								BindUtils
										.postNotifyChange(null, null, GestionPointagesViewModel.this, "listePointages");
							}
						}
					});
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void refuserAllPointage() {
		if (getListePointages() == null || getListePointages().size() == 0) {
			String message = "Il n'y a aucun pointage à refuser.";
			Messagebox.show(message, "Erreur", Messagebox.OK, "");
		} else if (getListePointages().size() == 1) {
			doChangeEtat(getListePointages(), EtatPointageEnum.REFUSE);
			filtrer();
			BindUtils.postNotifyChange(null, null, GestionPointagesViewModel.this, "listePointages");
		} else {
			// on ouvre une popup de confirmation
			Messagebox.show("Voulez-vous refuser les " + getListePointages().size() + " pointage(s) ?", "Confirmation",
					Messagebox.CANCEL | Messagebox.OK, "", new EventListener() {
						@Override
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								doChangeEtat(getListePointages(), EtatPointageEnum.REFUSE);
								filtrer();
								BindUtils
										.postNotifyChange(null, null, GestionPointagesViewModel.this, "listePointages");
							}
						}
					});
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "listePointages" })
	public void attenteAllPointage() {
		if (getListePointages() == null || getListePointages().size() == 0) {
			String message = "Il n'y a aucun pointage à re-mettre en saisie.";
			Messagebox.show(message, "Erreur", Messagebox.OK, "");
		} else if (getListePointages().size() == 1) {
			doChangeEtat(getListePointages(), EtatPointageEnum.SAISI);
			filtrer();
			BindUtils.postNotifyChange(null, null, GestionPointagesViewModel.this, "listePointages");
		} else {
			// on ouvre une popup de confirmation
			Messagebox.show("Voulez-vous re-mettre en saisie les " + getListePointages().size() + " pointage(s) ?",
					"Confirmation", Messagebox.CANCEL | Messagebox.OK, "", new EventListener() {
						@Override
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								doChangeEtat(getListePointages(), EtatPointageEnum.SAISI);
								filtrer();
								BindUtils
										.postNotifyChange(null, null, GestionPointagesViewModel.this, "listePointages");
							}
						}
					});
		}
	}

	@Command
	@NotifyChange({ "listePointages" })
	public void approuverPointage(@BindingParam("ref") ConsultPointageDto pointage) {
		doChangeEtat(Arrays.asList(pointage), EtatPointageEnum.APPROUVE);
		filtrer();
	}

	@Command
	@NotifyChange({ "listePointages" })
	public void refuserPointage(@BindingParam("ref") ConsultPointageDto pointage) {
		doChangeEtat(Arrays.asList(pointage), EtatPointageEnum.REFUSE);
		filtrer();
	}

	@Command
	@NotifyChange({ "listePointages" })
	public void attentePointage(@BindingParam("ref") ConsultPointageDto pointage) {
		doChangeEtat(Arrays.asList(pointage), EtatPointageEnum.SAISI);
		filtrer();
	}

	@Command
	public void modifierPointage(@BindingParam("ref") ConsultPointageDto pointage) {
		// on fait appel à la page saisie ptg en remplissant les param
		// create a window programmatically and use it as a modal dialog.
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("pointage", pointage);
		getDivDepart().getChildren().clear();
		Executions.createComponents("/pointages/saisie/saisieHebdo.zul", getDivDepart(), args);

	}

	private void sauvegardeEtatPointage(List<PointagesEtatChangeDto> listeChangeEtat) {
		ReturnMessageDto result = ptgWsConsumer.changerEtatPointage(currentUser.getAgent().getIdAgent(),
				listeChangeEtat);

		final HashMap<String, Object> map = new HashMap<String, Object>();
		List<ValidationMessage> listErreur = new ArrayList<ValidationMessage>();
		List<ValidationMessage> listInfo = new ArrayList<ValidationMessage>();
		// ici la liste info est toujours vide alors on ajoute un message
		if (result.getErrors().size() == 0)
			result.getInfos().add("Mise à jour des pointages correctement effectuée.");
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
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getTailleListe() {
		return tailleListe;
	}

	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
	}

	public List<ServiceDto> getListeServicesFiltre() {
		return listeServicesFiltre;
	}

	public void setListeServicesFiltre(List<ServiceDto> listeServicesFiltre) {
		this.listeServicesFiltre = listeServicesFiltre;
	}

	public ServiceDto getServiceFiltre() {
		return serviceFiltre;
	}

	public void setServiceFiltre(ServiceDto serviceFiltre) {
		this.serviceFiltre = serviceFiltre;
	}

	public Date getDateDebutFiltre() {
		return dateDebutFiltre;
	}

	public void setDateDebutFiltre(Date dateDebutFiltre) {
		this.dateDebutFiltre = dateDebutFiltre;
	}

	public Date getDateFinFiltre() {
		return dateFinFiltre;
	}

	public void setDateFinFiltre(Date dateFinFiltre) {
		this.dateFinFiltre = dateFinFiltre;
	}

	public List<RefEtatPointageDto> getListeEtatPointageFiltre() {
		return listeEtatPointageFiltre;
	}

	public void setListeEtatPointageFiltre(List<RefEtatPointageDto> listeEtatPointageFiltre) {
		this.listeEtatPointageFiltre = listeEtatPointageFiltre;
	}

	public RefEtatPointageDto getEtatPointageFiltre() {
		return etatPointageFiltre;
	}

	public void setEtatPointageFiltre(RefEtatPointageDto etatPointageFiltre) {
		this.etatPointageFiltre = etatPointageFiltre;
	}

	public List<RefTypePointageDto> getListeTypePointageFiltre() {
		return listeTypePointageFiltre;
	}

	public void setListeTypePointageFiltre(List<RefTypePointageDto> listeTypePointageFiltre) {
		this.listeTypePointageFiltre = listeTypePointageFiltre;
	}

	public RefTypePointageDto getTypePointageFiltre() {
		return typePointageFiltre;
	}

	public void setTypePointageFiltre(RefTypePointageDto typePointageFiltre) {
		this.typePointageFiltre = typePointageFiltre;
	}

	public List<AgentDto> getListeAgentsFiltre() {
		return listeAgentsFiltre;
	}

	public void setListeAgentsFiltre(List<AgentDto> listeAgentsFiltre) {
		this.listeAgentsFiltre = listeAgentsFiltre;
	}

	public AgentDto getAgentFiltre() {
		return agentFiltre;
	}

	public void setAgentFiltre(AgentDto agentFiltre) {
		this.agentFiltre = agentFiltre;
	}

	public List<ConsultPointageDto> getListePointages() {
		return listePointages;
	}

	public void setListePointages(List<ConsultPointageDto> listePointages) {
		this.listePointages = listePointages;
	}

	public ConsultPointageDto getPointageCourant() {
		return pointageCourant;
	}

	public void setPointageCourant(ConsultPointageDto pointageCourant) {
		this.pointageCourant = pointageCourant;
	}

	public AccessRightsPtgDto getDroitsPointage() {
		return droitsPointage;
	}

	public void setDroitsPointage(AccessRightsPtgDto droitsPointage) {
		this.droitsPointage = droitsPointage;
	}

	public List<String> getListeTypeHSupFiltre() {
		return listeTypeHSupFiltre;
	}

	public void setListeTypeHSupFiltre(List<String> listeTypeHSupFiltre) {
		this.listeTypeHSupFiltre = listeTypeHSupFiltre;
	}

	public String getTypeHSupFiltre() {
		return typeHSupFiltre;
	}

	public void setTypeHSupFiltre(String typeHSupFiltre) {
		this.typeHSupFiltre = typeHSupFiltre;
	}

	public Div getDivDepart() {
		return divDepart;
	}

	public void setDivDepart(Div divDepart) {
		this.divDepart = divDepart;
	}
}
