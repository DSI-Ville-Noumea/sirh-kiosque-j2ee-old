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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.RefEtatEnum;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.PrimeDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.SirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SaisieHebdomadaireViewModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	Listbox absenceListBox;

	@Command
	public void save(@BindingParam("ref") Listbox boxAbs) {
		System.out.println("ici");
		List<Listitem> t = boxAbs.getItems();
		for (Listitem a : t) {
			for (Component dto : a.getChildren()) {
				Listcell cell = (Listcell) dto;
				for (Component dto2 : cell.getChildren()) {
					try {
						Textbox text = (Textbox) dto2;
						System.out.println(" " + text.getValue());
					} catch (Exception e) {
						// on ne recupere pas les autres types
						try {
							Listbox box = (Listbox) dto2;
							for (Listitem item : box.getItems()) {
								for (Component cellBox : item.getChildren()) {
									Listcell cellMotif = (Listcell) cellBox;
									for (Component textBox : cellMotif.getChildren()) {
										Textbox text = (Textbox) textBox;
										if (text.getId().startsWith("motif")) {
											System.out.println("motif " + text.getValue());
										} else if (text.getId().startsWith("commentaire")) {
											System.out.println("commentaire " + text.getValue());
										}
									}
								}
							}
						} catch (Exception e2) {

						}
					}
				}
			}
		}
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		SirhPtgWSConsumer consu = new SirhPtgWSConsumer();
		consu.sirhPtgWsBaseUrl = "http://localhost:8090/sirh-ptg-ws/";
		// on recupere la fiche
		FichePointageDto result = consu.getFichePointageSaisie(9005138,
				getLundi(new SimpleDateFormat("dd/MM/yyyy").parse("10/11/2014")), 9003041);
		setFicheCourante(result);
		setJourPointage(getFicheCourante().getSaisies().get(0));

		// set models and render to listbox after comopsed
		absenceListBox.setModel(getAbsenceModel(getFicheCourante().getSaisies()));
		absenceListBox.setItemRenderer(new AbsenceListitemRenderer(getFicheCourante().getSaisies()));
	}

	public ListModel<AbsenceDto> getAbsenceModel(List<JourPointageDto> list) {
		List<AbsenceDto> l = new ArrayList<AbsenceDto>();

		l.add(new AbsenceDto());

		return new ListModelList<AbsenceDto>(l);
	}

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private ProfilAgentDto currentUser;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private FichePointageDto ficheCourante;

	private JourPointageDto jourPointage;

	/* POUR LES FILTRES */
	private Date dateLundi;
	private List<AgentDto> listeAgentsFiltre;
	private AgentDto agentFiltre;
	private List<ServiceDto> listeServicesFiltre;
	private ServiceDto serviceFiltre;
	private String dateFiltre;

	@Init
	public void initSaisieFichePointage() throws ParseException {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on charge les service pour les filtres
		List<ServiceDto> filtreService = ptgWsConsumer.getServicesPointages(currentUser.getAgent().getIdAgent());
		setListeServicesFiltre(filtreService);
		setDateFiltre("Semaine ... du ... au ...");
	}

	@Command
	public void enregistreFiche() {
		System.out.println("ici");
		getJourPointage();
		getFicheCourante();
	}

	@Command
	@NotifyChange({ "ficheCourante", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche",
			"jourPointage" })
	public void chargeFiche() throws ParseException {
		FichePointageDto result = ptgWsConsumer.getFichePointageSaisie(currentUser.getAgent().getIdAgent(),
				getLundi(getDateLundi()), getAgentFiltre().getIdAgent());
		setFicheCourante(result);
		setJourPointage(getFicheCourante().getSaisies().get(0));
	}

	@Command
	@NotifyChange({ "dateFiltre" })
	public void afficheSemaine() {
		if (getDateLundi() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(getDateLundi());
			String numSemaine = String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			String lundi = sdf.format(c.getTime());
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			String dimanche = sdf.format(c.getTime());

			setDateFiltre("Semaine " + numSemaine + " du " + lundi + " au " + dimanche);
		}
	}

	@Command
	@NotifyChange({ "listeAgentsFiltre", "agentFiltre" })
	public void afficheListeAgent() {
		setAgentFiltre(null);
		// on charge les agents pour les filtres
		List<AgentDto> filtreAgent = ptgWsConsumer.getAgentsPointages(currentUser.getAgent().getIdAgent(),
				getServiceFiltre().getCodeService());
		setListeAgentsFiltre(filtreAgent);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	@Command
	@NotifyChange({ "ficheCourante" })
	public void checkAccorde(@BindingParam("prime") PrimeDto prime, @BindingParam("checkbox") Checkbox checkbox) {
		prime.setQuantite(checkbox.isChecked() ? 1 : null);
	}

	public String etatToString(Integer idRefEtat) {
		return RefEtatEnum.getRefEtatEnum(idRefEtat).getLibEtat();
	}

	public boolean periodeHeure(String typeSaisie) {
		return typeSaisie.equals("PERIODE_HEURES");
	}

	public boolean caseACocher(String typeSaisie) {
		return typeSaisie.equals("CASE_A_COCHER");
	}

	public boolean nbHeures(String typeSaisie) {
		return typeSaisie.equals("NB_HEURES");
	}

	public boolean nbIndemnites(String typeSaisie) {
		return typeSaisie.equals("NB_INDEMNITES");
	}

	public boolean checkCoche(Integer quantite) {
		return quantite != null;
	}

	public String labelCoche(Integer quantite) {
		return quantite == null ? "Non" : "Oui";
	}

	public FichePointageDto getFicheCourante() {
		return ficheCourante;
	}

	public void setFicheCourante(FichePointageDto ficheCourante) {
		this.ficheCourante = ficheCourante;
	}

	private Date getLundi(Date dateLundi) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateLundi);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c.getTime();
	}

	public String getLundi() {
		if (getFicheCourante() == null)
			return "";
		return sdf.format(getFicheCourante().getDateLundi());
	}

	public String getMardi() {
		if (getFicheCourante() == null)
			return "";
		return getDatePlusJour(getFicheCourante().getDateLundi(), 1);
	}

	public String getMercredi() {
		if (getFicheCourante() == null)
			return "";
		return getDatePlusJour(getFicheCourante().getDateLundi(), 2);
	}

	public String getJeudi() {
		if (getFicheCourante() == null)
			return "";
		return getDatePlusJour(getFicheCourante().getDateLundi(), 3);
	}

	public String getVendredi() {
		if (getFicheCourante() == null)
			return "";
		return getDatePlusJour(getFicheCourante().getDateLundi(), 4);
	}

	public String getSamedi() {
		if (getFicheCourante() == null)
			return "";
		return getDatePlusJour(getFicheCourante().getDateLundi(), 5);
	}

	public String getDimanche() {
		if (getFicheCourante() == null)
			return "";
		return getDatePlusJour(getFicheCourante().getDateLundi(), 6);
	}

	private String getDatePlusJour(Date dateChoisi, int nbJours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateChoisi);
		cal.add(Calendar.DAY_OF_MONTH, nbJours);
		return sdf.format(cal.getTime());
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

	public String getDateFiltre() {
		return dateFiltre;
	}

	public void setDateFiltre(String dateFiltre) {
		this.dateFiltre = dateFiltre;
	}

	public Date getDateLundi() {
		return dateLundi;
	}

	public void setDateLundi(Date dateLundi) {
		this.dateLundi = dateLundi;
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

	public JourPointageDto getJourPointage() {
		return jourPointage;
	}

	public void setJourPointage(JourPointageDto jourPointage) {
		this.jourPointage = jourPointage;
	}


	
	public PrimeDto getJour(Integer i) {
		System.out.println("ici" + i);
		Listitem p = new Listitem();
		p.setValue(getFicheCourante().getSaisies().get(0).getPrimes().get(0));
		getFicheCourante().getSaisies().get(0).getPrimes().get(0).setMotif("motif nono");
		return getFicheCourante().getSaisies().get(0).getPrimes().get(0);
		
	}
}
