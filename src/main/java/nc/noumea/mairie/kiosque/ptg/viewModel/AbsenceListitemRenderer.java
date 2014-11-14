package nc.noumea.mairie.kiosque.ptg.viewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.kiosque.ptg.dto.AbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.ws.SirhPtgWSConsumer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

public class AbsenceListitemRenderer implements ListitemRenderer<Object> {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private List<JourPointageDto> listJourPtg;

	public AbsenceListitemRenderer(List<JourPointageDto> list) {
		this.listJourPtg = list;
	}

	public void render(Listitem listitem, Object value, int index) {

		// keep value in listitem
		listitem.setValue(value);

		// Vide
		addListCellListBoxVide(listitem);

		// Lundi
		addListCellListBox(listitem, getListJourPtg().get(0).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(0).getAbsences().get(index), 0, index);
		// Mardi
		addListCellListBox(listitem, getListJourPtg().get(1).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(1).getAbsences().get(index), 1, index);
		// Mercredi
		addListCellListBox(listitem, getListJourPtg().get(2).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(2).getAbsences().get(index), 2, index);
		// Jeudi
		addListCellListBox(listitem, getListJourPtg().get(3).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(3).getAbsences().get(index), 3, index);
		// Vendredi
		addListCellListBox(listitem, getListJourPtg().get(4).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(4).getAbsences().get(index), 4, index);
		// Samedi
		addListCellListBox(listitem, getListJourPtg().get(5).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(5).getAbsences().get(index), 5, index);
		// Dimanche
		addListCellListBox(listitem, getListJourPtg().get(6).getAbsences().size() == 0 ? new AbsenceDto()
				: getListJourPtg().get(6).getAbsences().get(index), 6, index);

		// create list headers while render first item
		if (index == 0) {
			renderListheads(listitem.getListbox());
		}
	}

	private void addListCellListBoxVide(Listitem listitem) {
		Listcell lc = new Listcell();
		Listbox boxAbs = new Listbox();

		// Ajout de la ligne etat
		Listitem ligneEtat = new Listitem();
		Listcell celluleEtat = new Listcell();
		Label labelEtat = new Label();
		labelEtat.setValue("Absences");
		celluleEtat.appendChild(labelEtat);
		ligneEtat.appendChild(celluleEtat);
		ligneEtat.setParent(boxAbs);
		// fin ajout etat

		// Ajout de la ligne heure debut/fin
		Listitem ligneHeure = new Listitem();
		Listcell celluleHeure = new Listcell();
		Label labelHeure = new Label();
		labelHeure.setValue("Début / fin *");
		celluleHeure.appendChild(labelHeure);
		ligneHeure.appendChild(celluleHeure);
		ligneHeure.setParent(boxAbs);
		// fin ajout heure debut/fin

		// Ajout de la ligne type abs
		Listitem ligneType = new Listitem();
		Listcell celluleType = new Listcell();
		Label labelType = new Label();
		labelType.setValue("Type *");
		celluleType.appendChild(labelType);
		ligneType.appendChild(celluleType);
		ligneType.setParent(boxAbs);
		// fin ajout Type abs

		// Ajout de la ligne motif
		Listitem ligneMotif = new Listitem();
		Listcell celluleMotif = new Listcell();
		Label labelMotif = new Label();
		labelMotif.setValue("Motif *");
		celluleMotif.appendChild(labelMotif);
		ligneMotif.appendChild(celluleMotif);
		ligneMotif.setParent(boxAbs);
		// fin ajout Motif

		// Ajout de la ligne commentaire
		Listitem ligneCommentaire = new Listitem();
		Listcell celluleCommentaire = new Listcell();
		Label labelCommentaire = new Label();
		labelCommentaire.setValue("Commentaire");
		celluleCommentaire.appendChild(labelCommentaire);
		ligneCommentaire.appendChild(celluleCommentaire);
		ligneCommentaire.setParent(boxAbs);
		// fin ajout commentaire

		boxAbs.setParent(lc);
		lc.setParent(listitem);

	}

	private void addListCellListBox(final Listitem listitem, final AbsenceDto abs, final int jour, final int index) {
		Listcell lc = new Listcell();
		Listbox boxAbs = new Listbox();

		// Ajout de la ligne etat
		Listitem ligneEtat = new Listitem();
		final Listcell celluleEtat = new Listcell();
		celluleEtat.setSclass("alignCenter");
		final Label labelEtat = new Label();
		labelEtat.setId("etatAbs_" + jour + ":" + index);
		labelEtat.setValue(abs.getIdRefEtat() == null ? "" : EtatPointageEnum.getEtatPointageEnum(abs.getIdRefEtat())
				.getLibEtat());
		Button buttonAddAbs = new Button();
		buttonAddAbs.setId("buttonAbs_" + jour + ":" + index);
		buttonAddAbs.setSclass("backSprite");
		buttonAddAbs.addEventListener(Events.ON_CLICK, new EventListener<MouseEvent>() {
			@Override
			public void onEvent(MouseEvent ie) throws Exception {
				Component target = ie.getTarget();
				if (target instanceof Button) {
					abs.setIdRefEtat(EtatPointageEnum.SAISI.getCodeEtat());
					labelEtat.setValue(abs.getIdRefEtat() == null ? "" : EtatPointageEnum.getEtatPointageEnum(
							abs.getIdRefEtat()).getLibEtat());
				}
			}
		});
		celluleEtat.appendChild(buttonAddAbs);
		celluleEtat.appendChild(labelEtat);
		ligneEtat.appendChild(celluleEtat);
		ligneEtat.setParent(boxAbs);
		// fin ajout etat

		// Ajout de la ligne heure debut/fin
		Listitem ligneHeure = new Listitem();
		Listcell celluleHeure = new Listcell();
		Timebox boxHeureFin = new Timebox();
		boxHeureFin.setId("finAbs_" + jour + ":" + index);
		boxHeureFin.setFormat("HH:mm");
		boxHeureFin.setValue(abs.getHeureFin());
		boxHeureFin.addEventListener(Events.ON_CHANGE, new EventListener<InputEvent>() {
			@Override
			public void onEvent(InputEvent ie) throws Exception {
				Component target = ie.getTarget();
				if (target instanceof Timebox) {
					abs.setHeureFin(((Timebox) target).getValue());
				}
			}
		});
		Timebox boxHeureDebut = new Timebox();
		boxHeureDebut.setId("debutAbs_" + jour + ":" + index);
		boxHeureDebut.setFormat("HH:mm");
		boxHeureDebut.setValue(abs.getHeureDebut());
		boxHeureDebut.addEventListener(Events.ON_CHANGE, new EventListener<InputEvent>() {
			@Override
			public void onEvent(InputEvent ie) throws Exception {
				Component target = ie.getTarget();
				if (target instanceof Timebox) {
					abs.setHeureDebut(((Timebox) target).getValue());
				}
			}
		});
		celluleHeure.appendChild(boxHeureDebut);
		celluleHeure.appendChild(boxHeureFin);
		ligneHeure.appendChild(celluleHeure);
		ligneHeure.setParent(boxAbs);
		// fin ajout heure debut/fin

		// Ajout de la ligne type absence
		Listitem ligneType = new Listitem();
		Listcell celluleType = new Listcell();
		final Combobox comboType = new Combobox();
		comboType.setId("typeAbs_" + jour + ":" + index);
		comboType.setButtonVisible(true);
		comboType.setModel(getModelTypeAbsence());
		comboType.setItemRenderer(new ComboitemRenderer<RefTypeAbsenceDto>() {
			@Override
			public void render(Comboitem item, RefTypeAbsenceDto data, int index) throws Exception {
				item.setLabel(data.getLibelle());
				item.setValue(data.getIdRefTypeAbsence());
				// set selected item
				if (abs.getIdRefTypeAbsence() != null) {
					if (abs.getIdRefTypeAbsence() == data.getIdRefTypeAbsence()) {
						comboType.setSelectedItem(item);
					}
				}
			}
		});
		comboType.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Component target = event.getTarget();
				if (target instanceof Combobox) {
					abs.setIdRefTypeAbsence(Integer.valueOf(((Combobox) target).getSelectedItem().getId()));
				}
			}
		});
		celluleType.appendChild(comboType);
		ligneType.appendChild(celluleType);
		ligneType.setParent(boxAbs);
		// fin ajout type absence

		// Ajout de la ligne motif
		Listitem ligneMotif = new Listitem();
		Listcell celluleMotif = new Listcell();
		Textbox boxMotif = new Textbox();
		boxMotif.setId("motifAbs_" + jour + ":" + index);
		boxMotif.setValue(abs.getMotif());
		boxMotif.addEventListener(Events.ON_CHANGE, new EventListener<InputEvent>() {
			@Override
			public void onEvent(InputEvent ie) throws Exception {
				Component target = ie.getTarget();
				if (target instanceof Textbox) {
					abs.setMotif(((Textbox) target).getValue());
				}
			}
		});
		celluleMotif.appendChild(boxMotif);
		ligneMotif.appendChild(celluleMotif);
		ligneMotif.setParent(boxAbs);
		// fin ajout Motif

		// Ajout de la ligne commentaire
		Listitem ligneCommentaire = new Listitem();
		Listcell celluleCommentaire = new Listcell();
		Textbox boxCommentaire = new Textbox();
		boxCommentaire.setId("commentaireAbs_" + jour + ":" + index);
		boxCommentaire.setRows(4);
		boxCommentaire.setValue(abs.getCommentaire());
		boxCommentaire.addEventListener(Events.ON_CHANGE, new EventListener<InputEvent>() {
			@Override
			public void onEvent(InputEvent ie) throws Exception {
				Component target = ie.getTarget();
				if (target instanceof Textbox) {
					abs.setCommentaire(((Textbox) target).getValue());
				}
			}
		});
		celluleCommentaire.appendChild(boxCommentaire);
		ligneCommentaire.appendChild(celluleCommentaire);
		ligneCommentaire.setParent(boxAbs);
		// fin ajout commentaire

		boxAbs.setParent(lc);
		lc.setParent(listitem);

	}

	private ListModel<RefTypeAbsenceDto> getModelTypeAbsence() {
		SirhPtgWSConsumer consu = new SirhPtgWSConsumer();
		consu.sirhPtgWsBaseUrl = "http://localhost:8090/sirh-ptg-ws/";
		List<RefTypeAbsenceDto> listeTypeAbs = consu.getListeRefTypeAbsence();
		List<String> res = new ArrayList<String>();
		for (RefTypeAbsenceDto ref : listeTypeAbs) {
			res.add(ref.getLibelle());
		}

		return new ListModelList<RefTypeAbsenceDto>(listeTypeAbs);
	}

	private void renderListheads(Listbox listbox) {
		Listhead lh = new Listhead();

		// En-tête vide
		Label labelVide = new Label();
		labelVide.setValue("");
		Listheader vide = new Listheader();
		vide.appendChild(labelVide);
		vide.setParent(lh);
		// Lundi
		Label labelLundi = new Label();
		labelLundi.setValue("Lundi");
		Label labelDateLundi = new Label();
		labelDateLundi.setValue(sdf.format(getListJourPtg().get(0).getDate()));
		Listheader lundi = new Listheader();
		lundi.appendChild(labelLundi);
		lundi.appendChild(new Separator());
		lundi.appendChild(labelDateLundi);
		lundi.setParent(lh);
		// Mardi
		Label labelMardi = new Label();
		labelMardi.setValue("Mardi");
		Label labelDateMardi = new Label();
		labelDateMardi.setValue(sdf.format(getListJourPtg().get(1).getDate()));
		Listheader mardi = new Listheader();
		mardi.appendChild(labelMardi);
		mardi.appendChild(new Separator());
		mardi.appendChild(labelDateMardi);
		mardi.setParent(lh);
		// Mercredi
		Label labelMercredi = new Label();
		labelMercredi.setValue("Mercredi");
		Label labelDateMercredi = new Label();
		labelDateMercredi.setValue(sdf.format(getListJourPtg().get(2).getDate()));
		Listheader mercredi = new Listheader();
		mercredi.appendChild(labelMercredi);
		mercredi.appendChild(new Separator());
		mercredi.appendChild(labelDateMercredi);
		mercredi.setParent(lh);
		// Jeudi
		Label labelJeudi = new Label();
		labelJeudi.setValue("Jeudi");
		Label labelDateJeudi = new Label();
		labelDateJeudi.setValue(sdf.format(getListJourPtg().get(3).getDate()));
		Listheader jeudi = new Listheader();
		jeudi.appendChild(labelJeudi);
		jeudi.appendChild(new Separator());
		jeudi.appendChild(labelDateJeudi);
		jeudi.setParent(lh);
		// Vendredi
		Label labelVendredi = new Label();
		labelVendredi.setValue("Vendredi");
		Label labelDateVendredi = new Label();
		labelDateVendredi.setValue(sdf.format(getListJourPtg().get(4).getDate()));
		Listheader vendredi = new Listheader();
		vendredi.appendChild(labelVendredi);
		vendredi.appendChild(new Separator());
		vendredi.appendChild(labelDateVendredi);
		vendredi.setParent(lh);
		// Samedi
		Label labelSamedi = new Label();
		labelSamedi.setValue("Samedi");
		Label labelDateSamedi = new Label();
		labelDateSamedi.setValue(sdf.format(getListJourPtg().get(5).getDate()));
		Listheader samedi = new Listheader();
		samedi.appendChild(labelSamedi);
		samedi.appendChild(new Separator());
		samedi.appendChild(labelDateSamedi);
		samedi.setParent(lh);
		// Dimanche
		Label labelDimanche = new Label();
		labelDimanche.setValue("Dimanche");
		Label labelDateDimanche = new Label();
		labelDateDimanche.setValue(sdf.format(getListJourPtg().get(6).getDate()));
		Listheader dimanche = new Listheader();
		dimanche.appendChild(labelDimanche);
		dimanche.appendChild(new Separator());
		dimanche.appendChild(labelDateDimanche);
		dimanche.setParent(lh);

		lh.setParent(listbox);
	}

	public List<JourPointageDto> getListJourPtg() {
		return listJourPtg;
	}

	public void setListJourPtg(List<JourPointageDto> listJourPtg) {
		this.listJourPtg = listJourPtg;
	}

}
