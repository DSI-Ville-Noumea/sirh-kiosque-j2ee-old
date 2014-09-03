package nc.noumea.mairie.kiosque.viewModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DemandesAgentViewModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private List<DemandeDto> listeDemandes;

	private DemandeDto demandeCourant;

	private Tab tabCourant;

	/* POUR LES FILTRES */
	private List<RefTypeAbsenceDto> listeTypeAbsenceFiltre;
	private RefTypeAbsenceDto typeAbsenceFiltre;
	private List<RefEtatDto> listeEtatAbsenceFiltre;
	private RefEtatDto etatAbsenceFiltre;
	private Date dateDebutFiltre;
	private Date dateFinFiltre;
	private Date dateDemandeFiltre;

	@Init
	public void initDemandes() {
		// on recharge les types d'absences pour les filtres
		List<RefTypeAbsenceDto> filtreFamille = absWsConsumer.getRefTypeAbsenceKiosque(9005138);
		setListeTypeAbsenceFiltre(filtreFamille);
		// on recharge les états d'absences pour les filtres
		List<RefEtatDto> filtreEtat = absWsConsumer.getEtatAbsenceKiosque("NON_PRISES");
		setListeEtatAbsenceFiltre(filtreEtat);
	}

	@Command
	@NotifyChange({ "listeDemandes", "listeEtatAbsenceFiltre" })
	public void changeVue(@BindingParam("tab") Tab tab) {
		setListeDemandes(null);
		// on recharge les états d'absences pour les filtres
		List<RefEtatDto> filtreEtat = absWsConsumer.getEtatAbsenceKiosque(tab.getId());
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
		List<DemandeDto> result = absWsConsumer.getDemandesAgent(9005138, getTabCourant().getId(),
				getDateDebutFiltre(), getDateFinFiltre(), getDateDemandeFiltre(), getEtatAbsenceFiltre() == null ? null
						: getEtatAbsenceFiltre().getIdRefEtat(), getTypeAbsenceFiltre() == null ? null
						: getTypeAbsenceFiltre().getIdRefTypeAbsence());
		setListeDemandes(result);
	}

	@Command
	@NotifyChange({ "typeAbsenceFiltre", "etatAbsenceFiltre", "dateDebutFiltre", "dateFinFiltre", "dateDemandeFiltre" })
	public void viderFiltre() {
		setTypeAbsenceFiltre(null);
		setEtatAbsenceFiltre(null);
		setDateDebutFiltre(null);
		setDateFinFiltre(null);
		setDateDemandeFiltre(null);
	}

	@Listen("onClick = #AJOUTER")
	public void ajouterDemande(Event e) {
		// create a window programmatically and use it as a modal dialog.
		Window win = (Window) Executions.createComponents("ajoutDemandeAgent.zul", null, null);
		win.doModal();
	}

	@Command
	public void modifierDemande() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("modifierDemandeAgent.zul", null, args);
		win.doModal();
	}

	@Command
	public void supprimerDemande() {
		// create a window programmatically and use it as a modal dialog.
		Map<String, DemandeDto> args = new HashMap<String, DemandeDto>();
		args.put("demandeCourant", getDemandeCourant());
		Window win = (Window) Executions.createComponents("supprimerDemandeAgent.zul", null, args);
		win.doModal();
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

	public List<RefEtatDto> getListeEtatAbsenceFiltre() {
		return listeEtatAbsenceFiltre;
	}

	public void setListeEtatAbsenceFiltre(List<RefEtatDto> listeEtatAbsenceFiltre) {
		this.listeEtatAbsenceFiltre = listeEtatAbsenceFiltre;
	}

	public RefEtatDto getEtatAbsenceFiltre() {
		return etatAbsenceFiltre;
	}

	public void setEtatAbsenceFiltre(RefEtatDto etatAbsenceFiltre) {
		this.etatAbsenceFiltre = etatAbsenceFiltre;
	}

	public Tab getTabCourant() {
		return tabCourant;
	}

	public void setTabCourant(Tab tabCourant) {
		this.tabCourant = tabCourant;
	}
}
