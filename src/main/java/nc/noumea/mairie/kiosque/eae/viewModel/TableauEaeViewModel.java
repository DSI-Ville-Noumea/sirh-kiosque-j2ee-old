package nc.noumea.mairie.kiosque.eae.viewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.validation.ValidationMessage;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TableauEaeViewModel {

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	private List<EaeListItemDto> tableauEae;

	private Div divDepart;

	/* POUR LE HAUT DU TABLEAU */
	private String filter;
	private String tailleListe;

	@Init
	public void initTableauEae(@ExecutionArgParam("div") Div div) {
		setDivDepart(div);
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupère les info du tableau des EAEs
		List<EaeListItemDto> tableau = eaeWsConsumer.getTableauEae(currentUser.getAgent().getIdAgent());
		setTableauEae(tableau);

		// on initialise la taille du tableau
		setTailleListe("5");
	}

	@Command
	public void modifierEae(@BindingParam("ref") EaeListItemDto eae) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("eae", eae);
		args.put("mode", "EDIT");
		getDivDepart().getChildren().clear();
		Executions.createComponents("/eae/onglet/eae.zul", getDivDepart(), args);
	}

	@Command
	public void voirEae(@BindingParam("ref") EaeListItemDto eae) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("eae", eae);
		args.put("mode", "VIEW");
		getDivDepart().getChildren().clear();
		Executions.createComponents("/eae/onglet/eae.zul", getDivDepart(), args);
	}

	@Command
	public void imprimerEae(@BindingParam("ref") EaeListItemDto eae) {
		// on imprime l'EAE de l'agent
		byte[] resp = eaeWsConsumer.imprimerEAE(eae.getIdEae());
		Filedownload.save(resp, "application/pdf", "eae_" + eae.getIdEae());
	}

	@Command
	@NotifyChange({ "tableauEae" })
	public void initialiserEae(@BindingParam("ref") EaeListItemDto eae) {

		ReturnMessageDto result = eaeWsConsumer.initialiseEae(currentUser.getAgent().getIdAgent(), eae.getAgentEvalue()
				.getIdAgent());

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
		// on re-affiche le tableau des EAEs
		filtrer();
	}

	@Command
	@NotifyChange({ "tableauEae" })
	public void doSearch() {
		List<EaeListItemDto> list = new ArrayList<EaeListItemDto>();
		if (getFilter() != null && !"".equals(getFilter())) {
			for (EaeListItemDto item : getTableauEae()) {
				// agent
				if (item.getAgentEvalue().getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgentEvalue().getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				// shd
				if (item.getAgentShd().getNom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				if (item.getAgentShd().getPrenom().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
				// etat
				if (item.getEtat().toLowerCase().contains(getFilter().toLowerCase())) {
					if (!list.contains(item))
						list.add(item);
				}
			}
			setTableauEae(list);
		} else {
			filtrer();
		}
	}

	public void filtrer() {
		// on recupère les info du tableau des EAEs
		List<EaeListItemDto> tableau = eaeWsConsumer.getTableauEae(currentUser.getAgent().getIdAgent());
		setTableauEae(tableau);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String getDateToString(Date date) {
		if (date == null)
			return "N/A";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	public List<EaeListItemDto> getTableauEae() {
		return tableauEae;
	}

	public void setTableauEae(List<EaeListItemDto> tableauEae) {
		this.tableauEae = tableauEae;
	}

	public String getTailleListe() {
		return tailleListe;
	}

	public void setTailleListe(String tailleListe) {
		this.tailleListe = tailleListe;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Div getDivDepart() {
		return divDepart;
	}

	public void setDivDepart(Div divDepart) {
		this.divDepart = divDepart;
	}
}
