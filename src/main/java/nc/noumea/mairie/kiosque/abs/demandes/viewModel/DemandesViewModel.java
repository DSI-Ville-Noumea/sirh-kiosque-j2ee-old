package nc.noumea.mairie.kiosque.abs.demandes.viewModel;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeEtatChangeDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatEnum;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeSaisiDto;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<DemandeDto> listeDemandes;

	private DemandeDto demandeCourant;

	private Tab tabCourant;

	/* POUR LES FILTRES */
	private List<RefGroupeAbsenceDto> listeGroupeAbsenceFiltre;
	private RefGroupeAbsenceDto groupeAbsenceFiltre;
	private List<RefTypeAbsenceDto> listeTypeAbsenceFiltre;
	private RefTypeAbsenceDto typeAbsenceFiltre;
	private List<RefEtatAbsenceDto> listeEtatAbsenceFiltre;
	private RefEtatAbsenceDto etatAbsenceFiltre;
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private Date dateDemandeFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private ProfilAgentDto currentUser;

	@Init
	public void initDemandes() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on recharge les groupes d'absences pour les filtres
		List<RefGroupeAbsenceDto> filtreGroupeFamille = absWsConsumer.getRefGroupeAbsence();
		setListeGroupeAbsenceFiltre(filtreGroupeFamille);

		// on charge les service pour les filtres
		List<ServiceDto> filtreService = absWsConsumer.getServicesAbsences(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		// pour les agents, on ne rempli pas la liste, elle le sera avec le
		// choix du service
		setListeAgentsFiltre(null);
		// on recharge les états d'absences pour les filtres
		List<RefEtatAbsenceDto> filtreEtat = absWsConsumer.getEtatAbsenceKiosque("NON_PRISES");
		setListeEtatAbsenceFiltre(filtreEtat);
		setTailleListe("5");
	}

	@Command
	public void visuSoldeAgent(@BindingParam("ref") AgentWithServiceDto agent) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, AgentWithServiceDto> args = new HashMap<String, AgentWithServiceDto>();
		args.put("agentCourant", agent);
		Window win = (Window) Executions.createComponents("/absences/demandes/demandesSoldeAgent.zul", null, args);
		win.doModal();
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre" })
	public void chargeAgent() {
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = absWsConsumer.getAgentsAbsences(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void doSearch() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<DemandeDto> list = new ArrayList<DemandeDto>();
		if (getFilter() != null && !"".equals(getFilter())) {
			for (DemandeDto item : getListeDemandes()) {
				if (item.getLibelleTypeDemande().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgentWithServiceDto().getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgentWithServiceDto().getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (sdf.format(item.getDateDebut()).contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setListeDemandes(list);
		} else {
			filtrer();
		}
	}

	@Command
	@NotifyChange({ "listeTypeAbsenceFiltre", "typeAbsenceFiltre" })
	public void alimenteTypeFamilleAbsence() {
		List<RefTypeAbsenceDto> filtreFamilleAbsence = absWsConsumer.getRefTypeAbsenceKiosque(getGroupeAbsenceFiltre()
				.getIdRefGroupeAbsence());
		if (filtreFamilleAbsence.size() == 1) {
			setListeTypeAbsenceFiltre(null);
			setTypeAbsenceFiltre(null);
		} else {
			setListeTypeAbsenceFiltre(filtreFamilleAbsence);
			setTypeAbsenceFiltre(null);
		}
	}

	@Command
	@NotifyChange({ "listeDemandes", "listeEtatAbsenceFiltre" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		setListeDemandes(null);
		// on recharge les états d'absences pour les filtres
		List<RefEtatAbsenceDto> filtreEtat = absWsConsumer.getEtatAbsenceKiosque(tab.getId());
		setListeEtatAbsenceFiltre(filtreEtat);
		// on sauvegarde l'onglet
		setTabCourant(tab);
		filtrer();
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void setTabDebut(@BindingParam("tab") Tab tab) {
		setTabCourant(tab);
		filtrer();
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void filtrer() {
		List<DemandeDto> result = absWsConsumer.getListeDemandes(currentUser.getAgent().getIdAgent(), getTabCourant()
				.getId(), getDateDebutFiltre(), getDateFinFiltre(), getDateDemandeFiltre(),
				getEtatAbsenceFiltre() == null ? null : getEtatAbsenceFiltre().getIdRefEtat(),
				getTypeAbsenceFiltre() == null ? null : getTypeAbsenceFiltre().getIdRefTypeAbsence(),
				getGroupeAbsenceFiltre() == null ? null : getGroupeAbsenceFiltre().getIdRefGroupeAbsence(),
				getAgentFiltre() == null ? null : getAgentFiltre().getIdAgent());
		setListeDemandes(result);
	}

	@GlobalCommand
	@NotifyChange({ "listeDemandes" })
	public void refreshListeDemande() {
		filtrer();
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void viserDemandeFavorable() {
		DemandeEtatChangeDto dto = new DemandeEtatChangeDto();
		dto.setIdDemande(getDemandeCourant().getIdDemande());
		dto.setIdRefEtat(RefEtatEnum.VISEE_FAVORABLE.getCodeEtat());
		dto.setDateAvis(new Date());

		ReturnMessageDto result = absWsConsumer.changerEtatDemandeAbsence(currentUser.getAgent().getIdAgent(), dto);

		if (result.getErrors().size() > 0 || result.getInfos().size() > 0) {
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
		}

		filtrer();
	}

	@Command
	public void viserDemandeDefavorable() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("/absences/demandes/viserDemande.zul", null, args);
		win.doModal();
	}

	@Command
	@NotifyChange({ "listeDemandes" })
	public void approuverDemande() {

		DemandeEtatChangeDto dto = new DemandeEtatChangeDto();
		dto.setIdDemande(getDemandeCourant().getIdDemande());
		dto.setIdRefEtat(RefEtatEnum.APPROUVEE.getCodeEtat());
		dto.setDateAvis(new Date());

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		ReturnMessageDto result = absWsConsumer.changerEtatDemandeAbsence(currentUser.getAgent().getIdAgent(), dto);

		if (result.getErrors().size() > 0 || result.getInfos().size() > 0) {
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
		}

		filtrer();
	}

	@Command
	public void desapprouverDemande() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("/absences/demandes/approuverDemande.zul", null, args);
		win.doModal();
	}

	@Command
	public void ajouterDemande() {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("/absences/demandes/ajoutDemande.zul", null, null);
		win.doModal();
	}

	@Command
	public void imprimerDemande() {
		// on imprime la demande
		byte[] resp = absWsConsumer.imprimerDemande(currentUser.getAgent().getIdAgent(), getDemandeCourant()
				.getIdDemande());
		Filedownload.save(resp, "application/pdf", "titreAbsence");
	}

	@Command
	public void annulerDemande() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("/absences/annulerDemande.zul", null, args);
		win.doModal();
	}

	@Command
	public void modifierDemande() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("/absences/modifierDemande.zul", null, args);
		win.doModal();
	}

	@Command
	public void supprimerDemande() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("/absences/supprimerDemande.zul", null, args);
		win.doModal();
	}

	@Command
	@NotifyChange({ "typeAbsenceFiltre", "etatAbsenceFiltre", "dateDebutFiltre", "dateFinFiltre", "dateDemandeFiltre",
			"listeAgentsFiltre", "agentFiltre", "serviceFiltre" })
	public void viderFiltre() {
		setListeAgentsFiltre(null);
		setServiceFiltre(null);
		setAgentFiltre(null);
		setTypeAbsenceFiltre(null);
		setEtatAbsenceFiltre(null);
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setDateDemandeFiltre(null);
	}

	@Command
	public void exportPDF(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfExporter exporter = new PdfExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("gestionDemandes.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	public void exportExcel(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("gestionDemandes.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	public String getEtatToString(Integer idRefEtat) {
		return RefEtatEnum.getRefEtatEnum(idRefEtat).getLibEtat();
	}

	public String getHeureDebutToString(DemandeDto dto) {
		if (dto.getTypeSaisi().getUniteDecompte().equals("jours")) {
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		return sf.format(dto.getDateDebut());
	}

	public String getDureeToString(Double duree, RefTypeSaisiDto typeSaisi) {
		if (typeSaisi != null) {
			if (typeSaisi.getUniteDecompte().equals("jours")) {
				return duree + " j";
			} else {
				return getHeureMinute(duree.intValue());
			}
		}
		return "";
	}

	private static String getHeureMinute(int nombreMinute) {
		int heure = nombreMinute / 60;
		int minute = nombreMinute % 60;
		String res = "";
		if (heure > 0)
			res += heure + "h";
		if (minute > 0)
			res += minute + "m";

		return res;
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
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

	public List<RefTypeAbsenceDto> getListeTypeAbsenceFiltre() {
		return listeTypeAbsenceFiltre;
	}

	public void setListeTypeAbsenceFiltre(List<RefTypeAbsenceDto> listeTypeAbsenceFiltre) {
		this.listeTypeAbsenceFiltre = listeTypeAbsenceFiltre;
	}

	public RefTypeAbsenceDto getTypeAbsenceFiltre() {
		return typeAbsenceFiltre;
	}

	public void setTypeAbsenceFiltre(RefTypeAbsenceDto typeAbsenceFiltre) {
		this.typeAbsenceFiltre = typeAbsenceFiltre;
	}

	public List<RefEtatAbsenceDto> getListeEtatAbsenceFiltre() {
		return listeEtatAbsenceFiltre;
	}

	public void setListeEtatAbsenceFiltre(List<RefEtatAbsenceDto> listeEtatAbsenceFiltre) {
		this.listeEtatAbsenceFiltre = listeEtatAbsenceFiltre;
	}

	public RefEtatAbsenceDto getEtatAbsenceFiltre() {
		return etatAbsenceFiltre;
	}

	public void setEtatAbsenceFiltre(RefEtatAbsenceDto etatAbsenceFiltre) {
		this.etatAbsenceFiltre = etatAbsenceFiltre;
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

	public Date getDateDemandeFiltre() {
		return dateDemandeFiltre;
	}

	public void setDateDemandeFiltre(Date dateDemandeFiltre) {
		this.dateDemandeFiltre = dateDemandeFiltre;
	}

	public List<DemandeDto> getListeDemandes() {
		return listeDemandes;
	}

	public void setListeDemandes(List<DemandeDto> listeDemandes) {
		this.listeDemandes = listeDemandes;
	}

	public DemandeDto getDemandeCourant() {
		return demandeCourant;
	}

	public void setDemandeCourant(DemandeDto demandeCourant) {
		this.demandeCourant = demandeCourant;
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

	public List<RefGroupeAbsenceDto> getListeGroupeAbsenceFiltre() {
		return listeGroupeAbsenceFiltre;
	}

	public void setListeGroupeAbsenceFiltre(List<RefGroupeAbsenceDto> listeGroupeAbsenceFiltre) {
		this.listeGroupeAbsenceFiltre = listeGroupeAbsenceFiltre;
	}

	public RefGroupeAbsenceDto getGroupeAbsenceFiltre() {
		return groupeAbsenceFiltre;
	}

	public void setGroupeAbsenceFiltre(RefGroupeAbsenceDto groupeAbsenceFiltre) {
		this.groupeAbsenceFiltre = groupeAbsenceFiltre;
	}

}
