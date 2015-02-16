package nc.noumea.mairie.kiosque.abs.agent.viewModel;

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
import nc.noumea.mairie.kiosque.abs.dto.RefEtatAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatEnum;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeSaisiCongeAnnuelDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeSaisiDto;
import nc.noumea.mairie.kiosque.export.ExcelExporter;
import nc.noumea.mairie.kiosque.export.PdfExporter;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
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
public class DemandesAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<DemandeDto> listeDemandes;

	private Tab tabCourant;

	/* POUR LES FILTRES */
	private List<RefGroupeAbsenceDto> listeGroupeAbsenceFiltre;
	private RefGroupeAbsenceDto groupeAbsenceFiltre;
	private List<RefTypeAbsenceDto> listeTypeAbsenceFiltre;
	private RefTypeAbsenceDto typeAbsenceFiltre;
	private List<RefEtatAbsenceDto> listeEtatAbsenceFiltre;
	private RefEtatAbsenceDto etatAbsenceFiltre;
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private Date dateDemandeFiltre;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	private ProfilAgentDto currentUser;

	@Init
	public void initDemandesAgent() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");

		// on recharge les groupes d'absences pour les filtres
		List<RefGroupeAbsenceDto> filtreGroupeFamille = absWsConsumer.getRefGroupeAbsence();
		setListeGroupeAbsenceFiltre(filtreGroupeFamille);

		// on recharge les états d'absences pour les filtres
		List<RefEtatAbsenceDto> filtreEtat = absWsConsumer.getEtatAbsenceKiosque("NON_PRISES");
		setListeEtatAbsenceFiltre(filtreEtat);
		setTailleListe("5");
	}

	@Command
	@NotifyChange({ "listeTypeAbsenceFiltre", "typeAbsenceFiltre" })
	public void alimenteTypeFamilleAbsence() {
		List<RefTypeAbsenceDto> filtreFamilleAbsence = absWsConsumer.getRefTypeAbsenceKiosque(getGroupeAbsenceFiltre()
				.getIdRefGroupeAbsence(), currentUser.getAgent().getIdAgent());
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
		List<DemandeDto> result = absWsConsumer.getDemandesAgent(currentUser.getAgent().getIdAgent(), getTabCourant()
				.getId(), getDateDebutFiltre(), getDateFinFiltre(), getDateDemandeFiltre(),
				getEtatAbsenceFiltre() == null ? null : getEtatAbsenceFiltre().getIdRefEtat(),
				getTypeAbsenceFiltre() == null ? null : getTypeAbsenceFiltre().getIdRefTypeAbsence(),
				getGroupeAbsenceFiltre() == null ? null : getGroupeAbsenceFiltre().getIdRefGroupeAbsence());
		setListeDemandes(result);
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
	@NotifyChange({ "groupeAbsenceFiltre", "listeTypeAbsenceFiltre", "typeAbsenceFiltre", "etatAbsenceFiltre",
			"dateDebutFiltre", "dateFinFiltre", "dateDemandeFiltre" })
	public void viderFiltre() {
		setListeTypeAbsenceFiltre(null);
		setGroupeAbsenceFiltre(null);
		setTypeAbsenceFiltre(null);
		setEtatAbsenceFiltre(null);
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setDateDemandeFiltre(null);
	}

	public List<DemandeDto> getHistoriqueAbsence(DemandeDto abs) {
		List<DemandeDto> result = absWsConsumer.getHistoriqueAbsence(currentUser.getAgent().getIdAgent(),
				abs.getIdDemande());
		return result;
	}

	@Command
	public void ajouterDemande() {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("/absences/agent/ajoutDemandeAgent.zul", null, null);
		win.doModal();
	}

	@Command
	public void modifierDemande(@BindingParam("ref") DemandeDto demande) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", demande);
		Window win = (Window) Executions.createComponents("/absences/modifierDemande.zul", null, args);
		win.doModal();
	}

	@Command
	public void supprimerDemande(@BindingParam("ref") DemandeDto demande) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", demande);
		Window win = (Window) Executions.createComponents("/absences/supprimerDemande.zul", null, args);
		win.doModal();
	}

	@Command
	public void annulerDemande(@BindingParam("ref") DemandeDto demande) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", demande);
		Window win = (Window) Executions.createComponents("/absences/annulerDemande.zul", null, args);
		win.doModal();
	}

	@Command
	public void imprimerDemande(@BindingParam("ref") DemandeDto demande) {
		// on imprime la demande
		byte[] resp = absWsConsumer.imprimerDemande(currentUser.getAgent().getIdAgent(), demande.getIdDemande());
		Filedownload.save(resp, "application/pdf", "titreAbsence");
	}

	@GlobalCommand
	@NotifyChange({ "listeDemandes" })
	public void refreshListeDemande() {
		filtrer();
	}

	@Command
	public void exportPDF(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfExporter exporter = new PdfExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("mesDemandes.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	@Command
	public void exportExcel(@BindingParam("ref") Listbox listbox) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExcelExporter exporter = new ExcelExporter();
		exporter.export(listbox, out);

		AMedia amedia = new AMedia("mesDemandes.xlsx", "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
	}

	public String dateSaisieToString(Date dateSaisie) {
		SimpleDateFormat sdfJour = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
		return sdfJour.format(dateSaisie) + " à " + sdfHeure.format(dateSaisie);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String getEtatToString(Integer idRefEtat) {
		return RefEtatEnum.getRefEtatEnum(idRefEtat).getLibEtat();
	}

	public String getDateDebutToString(DemandeDto dto) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String res = sdf.format(dto.getDateDebut());
		if (dto.getTypeSaisi() != null && dto.getTypeSaisi().getUniteDecompte().equals("jours")) {
			if (dto.isDateDebutAM()) {
				res += " - M";
			} else if (dto.isDateDebutPM()) {
				res += " - AM";
			}
			return res;
		}
		if (dto.getTypeSaisiCongeAnnuel() != null) {
			if (dto.isDateDebutAM()) {
				res += " - M";
			} else if (dto.isDateDebutPM()) {
				res += " - AM";
			}
			return res;
		}
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		return res + " - " + sf.format(dto.getDateDebut());
	}

	public String getDateFinToString(DemandeDto dto) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String res = sdf.format(dto.getDateFin());
		if (dto.getTypeSaisi() != null && dto.getTypeSaisi().getUniteDecompte().equals("jours")) {
			if (dto.isDateFinAM()) {
				res += " - M";
			} else if (dto.isDateFinPM()) {
				res += " - AM";
			}
			return res;
		}
		if (dto.getTypeSaisiCongeAnnuel() != null) {
			if (dto.isDateFinAM()) {
				res += " - M";
			} else if (dto.isDateFinPM()) {
				res += " - AM";
			}
			return res;
		}
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		return res + " - " + sf.format(dto.getDateFin());
	}

	public String getDureeToString(Double duree, RefTypeSaisiDto typeSaisi,
			RefTypeSaisiCongeAnnuelDto typeSaisiCongeAnnuel) {
		if (typeSaisi != null) {
			if (typeSaisi.getUniteDecompte().equals("jours")) {
				return duree + " j";
			} else {
				return getHeureMinute(duree.intValue());
			}
		}
		if (typeSaisiCongeAnnuel != null) {
			return duree + " j";
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

	public List<DemandeDto> getListeDemandes() {
		return listeDemandes;
	}

	public void setListeDemandes(List<DemandeDto> listeDemandes) {
		this.listeDemandes = listeDemandes;
	}

	public RefTypeAbsenceDto getTypeAbsenceFiltre() {
		return typeAbsenceFiltre;
	}

	public void setTypeAbsenceFiltre(RefTypeAbsenceDto typeAbsenceFiltre) {
		this.typeAbsenceFiltre = typeAbsenceFiltre;
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

	public List<RefTypeAbsenceDto> getListeTypeAbsenceFiltre() {
		return listeTypeAbsenceFiltre;
	}

	public void setListeTypeAbsenceFiltre(List<RefTypeAbsenceDto> listeTypeAbsenceFiltre) {
		this.listeTypeAbsenceFiltre = listeTypeAbsenceFiltre;
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

	@NotifyChange
	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
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
