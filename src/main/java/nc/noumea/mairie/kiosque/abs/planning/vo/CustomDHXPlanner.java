package nc.noumea.mairie.kiosque.abs.planning.vo;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nc.noumea.mairie.ads.dto.EntiteDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;

import org.joda.time.DateTime;

import com.dhtmlx.planner.DHXExportMode;
import com.dhtmlx.planner.DHXPlanner;
import com.dhtmlx.planner.DHXSkin;
import com.dhtmlx.planner.controls.DHXLocalization;
import com.dhtmlx.planner.controls.DHXTimelineUnit;
import com.dhtmlx.planner.controls.DHXTimelineView;
import com.dhtmlx.planner.data.DHXDataFormat;
import com.dhtmlx.planner.extensions.DHXExtension;

public class CustomDHXPlanner extends DHXPlanner {

	public CustomDHXPlanner(String codeBase, DHXSkin skin, String urlEvents, List<AgentWithServiceDto> listAgents) {
		super(codeBase, skin);

		// ////////////////////////////////////////////////////////
		// //////////// CREATION DU PLANNING //////////////////////
		// ////////////////////////////////////////////////////////

		// on supprime les vues pare defaut (day/week/month)
		this.views.clear();

		// ajoute la librairie pour le TimeLine
		this.extensions.add(DHXExtension.TREE_TIMELINE);

		this.setInitialView("viewMois");
		this.setInitialDate(new DateTime().withDayOfMonth(1).toDate());
		this.setWidth(1252, "px");
		this.load(urlEvents, DHXDataFormat.JSON);
		this.setHeight(100, "%");

		this.config.setReadonly(true);
		this.config.setScrollHour(6);

		this.localizations.set(DHXLocalization.French);

		this.extensions.add(DHXExtension.CONTAINER_AUTORESIZE);
		this.extensions.add(DHXExtension.PDF);
		
		this.setPdfMode(DHXExportMode.FULLCOLOR);
		this.toPDF("/planning");

		// ////////////////////////////////////////////////////////
		// //////////// pour les infobulles /////////////////////
		this.extensions.add(DHXExtension.TOOLTIP);

		String toolTipText = "<b>Agent :</b> {textInfoBulle}" + "<br/><b>Groupe d'absence :</b> {typeAbsence}"
				+ "<br/><b>Etat :</b> {etatAbsence}" + "<br/><b>Date de début :</b> {dateDebut}"
				+ "<br/><b>Date de fin :</b> {dateFin}";
		this.templates.setTooltipText(toolTipText);

		// ////////////////////////////////////////////////////////
		// //////////// CREATION DU TIMELINE //////////////////////
		// details des proprietes :
		// http://docs.dhtmlx.com/scheduler/api__scheduler_createtimelineview.html
		// ////////////////////////////////////////////////////////
		// /////// 1er timeline : par semaine /////////////////////

		// /!\ attention le nom de la DHXTimelineView doit etre ajoute dans
		// CustomEventsManager.getCollections()
		DHXTimelineView viewSemaine = new DHXTimelineView("viewSemaine", "user", "Semaine");
		// mode d affichage
		viewSemaine.setRenderMode(DHXTimelineView.RenderModes.TREE);
		// unite
		viewSemaine.setXScaleUnit(DHXTimelineView.XScaleUnits.DAY);
		// CSS pour le bouton "Semaine"
		viewSemaine.setTabStyle("margin-left:-190px;");
		// DateTimeFormat des jours
		viewSemaine.setXDate("%d");
		// interval entre chaque colonne : ici 1 jour (tous les jours)
		viewSemaine.setXStep(1);
		// nombre de colonnes
		viewSemaine.setXSize(7);
		// propriete utilisee par le timeline pour matcher avec les
		// CustomDHXEvent
		// la propriete doit etre egale au nom de l attribut CustomDHXEvent.user
		viewSemaine.setYProperty("user");
		// taille des groupes dans le cas du TREE_MODE
		viewSemaine.setFolderDy(16);
		// taille des cellules "Prenom Nom des agents" (1er colonne)
		viewSemaine.setDx(280);
		viewSemaine.setDy(30);
		// hauteur des events des agents
		viewSemaine.setEventDy(26);

		// dois correspondre au nom du DHXTimelineView (ci-dessus)
		viewSemaine.setServerList("viewSemaine");
		viewSemaine.addSecondScale(DHXTimelineView.XScaleUnits.MONTH, "%F");

		Collections.sort(listAgents);
		List<EntiteDto> listeServices = getListServices(listAgents);
		// on trie les services par ordre alpha
		Collections.sort(listeServices);

		for (EntiteDto service : listeServices) {
			DHXTimelineUnit section = new DHXTimelineUnit(service.getIdEntite(), service.getLabel());// defines
																											// the
																											// header
																											// of
																											// the
																											// folder

			viewSemaine.addOption(section);
			for (AgentWithServiceDto agent : listAgents) {
				if (null != service.getIdEntite()
						&& null != agent.getIdServiceADS()
						&& service.getIdEntite().equals(agent.getIdServiceADS())) {
					section.addOption(new DHXTimelineUnit(agent.getIdAgent().toString(), agent.getNom() + " "
							+ agent.getPrenom()));
				}
			}
		}

		this.views.add(viewSemaine);

		// ////////////////////////////////////////////////////////
		// //////////// CREATION DU TIMELINE //////////////////////
		// details des proprietes :
		// http://docs.dhtmlx.com/scheduler/api__scheduler_createtimelineview.html
		// ////////////////////////////////////////////////////////
		// /////// 2e timeline : par mois /////////////////////

		// /!\ attention le nom de la DHXTimelineView doit etre ajoute dans
		// CustomEventsManager.getCollections()
		DHXTimelineView viewMois = new DHXTimelineView("viewMois", "user", "Mois");
		// mode d affichage
		viewMois.setRenderMode(DHXTimelineView.RenderModes.TREE);
		// unite
		viewMois.setXScaleUnit(DHXTimelineView.XScaleUnits.DAY);
		// CSS pour le bouton "Mois"
		viewMois.setTabStyle("margin-left:-190px;");
		// DateTimeFormat des jours
		viewMois.setXDate("%d");
		// interval entre chaque colonne : ici 1 jour (tous les jours)
		viewMois.setXStep(1);
		// nombre de colonnes
		viewMois.setXSize(31);
		// propriete utilisee par le timeline pour matcher avec les
		// CustomDHXEvent
		// la propriete doit etre egale au nom de l attribut CustomDHXEvent.user
		viewMois.setYProperty("user");
		// taille des groupes dans le cas du TREE_MODE
		viewMois.setFolderDy(16);
		// taille des cellules "Prenom Nom des agents" (1er colonne)
		viewMois.setDx(280);
		viewMois.setDy(30);
		// hauteur des events des agents
		viewMois.setEventDy(26);

		// dois correspondre au nom du DHXTimelineView (ci-dessus)
		viewMois.setServerList("viewMois");
		viewMois.addSecondScale(DHXTimelineView.XScaleUnits.MONTH, "%F");

		// TREE MODE
		viewMois.setFolderEventsAvailable(false);

		for (EntiteDto service : listeServices) {
			DHXTimelineUnit section = new DHXTimelineUnit(service.getIdEntite(), service.getLabel());// defines
																											// the
																											// header
																											// of
																											// the
																											// folder

			viewMois.addOption(section);
			for (AgentWithServiceDto agent : listAgents) {
				if (null != service.getIdEntite()
						&& null != agent.getIdServiceADS()
						&& service.getIdEntite().equals(agent.getIdServiceADS())) {
					section.addOption(new DHXTimelineUnit(agent.getIdAgent().toString(), agent.getNom() + " "
							+ agent.getPrenom()));
				}
			}
		}

		this.views.add(viewMois);

		// ////////////////////////////////////////////////////////
		// //////////// CREATION DU TIMELINE //////////////////////
		// details des proprietes :
		// http://docs.dhtmlx.com/scheduler/api__scheduler_createtimelineview.html
		// ////////////////////////////////////////////////////////
		// /////// 3e timeline : par trimestre /////////////////////

		// /!\ attention le nom de la DHXTimelineView doit etre ajoute dans
		// CustomEventsManager.getCollections()
		DHXTimelineView viewTrimestre = new DHXTimelineView("viewTrimestre", "user", "Trimestre");
		// mode d affichage
		viewTrimestre.setRenderMode(DHXTimelineView.RenderModes.TREE);
		// unite
		viewTrimestre.setXScaleUnit(DHXTimelineView.XScaleUnits.DAY);
		// CSS pour le bouton "Trimestre"
		viewTrimestre.setTabStyle("margin-left:-190px;");
		// DateTimeFormat des jours
		viewTrimestre.setXDate("%d");
		// interval entre chaque colonne : ici 7 jours
		viewTrimestre.setXStep(7);
		// nombre de colonnes
		viewTrimestre.setXSize(13);
		// propriete utilisee par le timeline pour matcher avec les
		// CustomDHXEvent
		// la propriete doit etre egale au nom de l attribut CustomDHXEvent.user
		viewTrimestre.setYProperty("user");
		// taille des groupes dans le cas du TREE_MODE
		viewTrimestre.setFolderDy(16);
		// taille des cellules "Prenom Nom des agents" (1er colonne)
		viewTrimestre.setDx(280);
		viewTrimestre.setDy(30);
		// hauteur des events des agents
		viewTrimestre.setEventDy(26);

		// dois correspondre au nom du DHXTimelineView (ci-dessus)
		viewTrimestre.setServerList("viewTrimestre");
		viewTrimestre.addSecondScale(DHXTimelineView.XScaleUnits.MONTH, "%F");

		for (EntiteDto service : listeServices) {
			DHXTimelineUnit section = new DHXTimelineUnit(service.getIdEntite(), service.getLabel());// defines
																											// the
																											// header
																											// of
																											// the
																											// folder

			viewTrimestre.addOption(section);
			for (AgentWithServiceDto agent : listAgents) {
				if (null != service.getIdEntite()
						&& null != agent.getIdServiceADS()
						&& service.getIdEntite().equals(agent.getIdServiceADS())) {
					section.addOption(new DHXTimelineUnit(agent.getIdAgent().toString(), agent.getNom() + " "
							+ agent.getPrenom()));
				}
			}
		}

		this.views.add(viewTrimestre);
	}

	private List<EntiteDto> getListServices(List<AgentWithServiceDto> listAgents) {

		List<EntiteDto> listServices = new ArrayList<EntiteDto>();
		for (AgentWithServiceDto agent : listAgents) {
			EntiteDto service = new EntiteDto();
			service.setIdEntite(agent.getIdServiceADS());
			service.setLabelCourt(agent.getService());
			service.setLabel(agent.getService());
			if (!listServices.contains(service)) {
				listServices.add(service);
			}
		}
		return listServices;
	}

	@Override
	public String render() throws Exception {
		return "<div id='divChild'> <html>"
				+ super.render().replace("&nbsp;", "").replace("&", "")
						.replace("type=\"text/css\" charset=\"utf-8\">", "type=\"text/css\" charset=\"utf-8\" />") + ""
				+ "</html></div>";
	}
}
