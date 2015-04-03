package nc.noumea.mairie.kiosque.viewModel;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsAbsDto;
import nc.noumea.mairie.kiosque.authentification.IAccueilService;
import nc.noumea.mairie.kiosque.cmis.ISharepointService;
import nc.noumea.mairie.kiosque.dto.AccueilRhDto;
import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.EtatPointageEnum;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Div;
import org.zkoss.zul.Panel;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AccueilViewModel extends SelectorComposer<Component> {

	private Logger logger = LoggerFactory.getLogger(AccueilViewModel.class);

	@WireVariable
	private ISirhWSConsumer sirhWsConsumer;

	@WireVariable
	private IAccueilService accueilService;

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	@WireVariable
	private ISirhEaeWSConsumer eaeWsConsumer;

	@WireVariable
	private ISharepointService sharepointConsumer;
	
	private ProfilAgentDto currentUser;

	private List<AccueilRhDto> listeTexteAccueil;

	private ReferentRhDto refrentRh;
	
	private boolean isRecette;

	private String nombreAbsenceAApprouver = "";

	private String nombreAbsenceAViser = "";
	
	private String nombrePointageAApprouver = "";
	
	private String nombreEAEaRealiser = "";

	private AccessRightsAbsDto droitsAbsence;

	private AccessRightsPtgDto droitsPointage;

	private boolean droitsEae;
	
	@Wire
	private Portallayout portalLayout;

	@Listen("onPortalMove = #portalLayout")
    public void saveStatus() {
        int i = 0;
        for (Component portalChild : portalLayout.getChildren()) {
            List<String> portletIds = new ArrayList<String>();
            for (Component portlet : portalChild.getChildren())
                portletIds.add(portlet.getId());
            Executions.getCurrent().getSession().setAttribute("PortalChildren" + i++, portletIds);
        }
    }
     
    @Listen("onCreate = #portalLayout")
    public void initStatus() {
         
        List<? extends Component> panelchildren = portalLayout.getChildren();
        for (int i = 0; i < panelchildren.size(); i++) {
            List<String> panelIds = (List<String>) Executions.getCurrent().getSession().getAttribute("PortalChildren" + i);
            if (panelIds != null) {
                for (String panelId : panelIds) {
                    Panel newPanel = (Panel)portalLayout.getFellow(panelId);
                    if (panelchildren.size() > 0)
                        panelchildren.get(i).insertBefore(newPanel, panelchildren.get(0));
                    else
                        newPanel.setParent(panelchildren.get(i));
                     
                }
            }
        }
    }
    
	@Init
	public void initAccueil() {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// message d'accueil
		List<AccueilRhDto> listeTexte = sirhWsConsumer.getListeTexteAccueil();
		setListeTexteAccueil(listeTexte);
		// refrent Rh de l'agent
		ReferentRhDto referent = sirhWsConsumer.getReferentRH(currentUser.getAgent().getIdAgent());
		setRefrentRh(referent);
		
		if (accueilService.getEnvironnement().equals("RECETTE")) {
			setRecette(true);
		} else {
			setRecette(false);
		}	
		
		/* Pour les absences */
		try {
			AccessRightsAbsDto droitsAbsence = absWsConsumer.getDroitsAbsenceAgent(currentUser.getAgent().getIdAgent());
			setDroitsAbsence(droitsAbsence);
		} catch (Exception e) {
			// l'appli SIRH-ABS-WS ne semble pas répondre
			logger.error("L'application SIRH-ABS-WS ne répond pas.");
		}
		/* Pour les eaes */
		try {
			boolean droitsEAe = sirhWsConsumer.estHabiliteEAE(currentUser.getAgent().getIdAgent());
			setDroitsEae(droitsEAe);
		} catch (Exception e) {
			// l'appli SIRH-EAE-WS ne semble pas répondre
			logger.error("L'application SIRH-EAE-WS ne répond pas.");
		}
		/* Pour les pointages */
		try {
			AccessRightsPtgDto droitsPointage = ptgWsConsumer.getListAccessRightsByAgent(currentUser.getAgent()
					.getIdAgent());
			setDroitsPointage(droitsPointage);
		} catch (Exception e) {
			// l'appli SIRH-PTG-WS ne semble pas répondre
			logger.error("L'application SIRH-PTG-WS ne répond pas.");
		}
		
		if(null != getDroitsAbsence()
				&& getDroitsAbsence().isApprouverModif()) {
			Integer nbrAbs = new Integer(absWsConsumer.countDemandesAApprouver(currentUser.getAgent().getIdAgent()));
			
			String nbrAbsStr = "";
			if(0 == nbrAbs) {
				nbrAbsStr = "Vous n'avez pas de demande d'absence à approuver."; 
			}else if(1 == nbrAbs) {
				nbrAbsStr = "Vous avez " + nbrAbs + " demande d'absence à approuver.";
			}else{
				nbrAbsStr = "Vous avez " + nbrAbs + " demandes d'absence à approuver.";
			}
			setNombreAbsenceAApprouver(nbrAbsStr);
		}
		
		if(null != getDroitsAbsence()
				&& getDroitsAbsence().isViserModif()) {
			Integer nbrAbs = new Integer(absWsConsumer.countDemandesAViser(currentUser.getAgent().getIdAgent()));
			
			String nbrAbsViserStr = "";
			if(0 == nbrAbs) {
				nbrAbsViserStr = "Vous n'avez pas de demande d'absence à viser."; 
			}else if(1 == nbrAbs) {
				nbrAbsViserStr = "Vous avez " + nbrAbs + " demande d'absence à viser.";
			}else{
				nbrAbsViserStr = "Vous avez " + nbrAbs + " demandes d'absence à viser.";
			}
			setNombreAbsenceAViser(nbrAbsViserStr);
		}
		if(isDroitsEae()) {
			String nbrEaeStr = "";
			try {
				List<EaeDashboardItemDto> tableau = eaeWsConsumer.getTableauBord(currentUser.getAgent().getIdAgent());
				int nbrEae = null != tableau ? tableau.size() : 0;
				if(0 == nbrEae) {
					nbrEaeStr = "Vous n'avez pas de EAE à réaliser."; 
				}else if(1 == nbrEae) {
					nbrEaeStr = "Vous avez " + nbrEae + " EAE à réaliser.";
				}else{
					nbrEaeStr = "Vous avez " + nbrEae + " EAEs à réaliser.";
				}
			} catch(Exception e) {
				// l'appli SIRH-EAE-WS ne semble pas répondre
				logger.error("Une erreur est survenue avec l'application SIRH-EAE-WS : " + e.getMessage());
				nbrEaeStr = "Une erreur est survenue.";
			}
			setNombreEAEaRealiser(nbrEaeStr);
		}
		if(null != getDroitsPointage()
				&& getDroitsPointage().isApprobation()) {
			Date toDate = new Date();
			DateTime fromDate = new DateTime(toDate);
			fromDate = fromDate.minusMonths(3);
			
			List<ConsultPointageDto> listPtg = 
					ptgWsConsumer.getListePointages(
							currentUser.getAgent().getIdAgent(), fromDate.toDate(), toDate, null, null, 
							EtatPointageEnum.SAISI.getCodeEtat(), null, null);
			int nbrPtg = null != listPtg ? listPtg.size() : 0;
			String nbrPtgStr = "";
			if(0 == nbrPtg) {
				nbrPtgStr = "Vous n'avez pas de pointage à approuver."; 
			}else if(1 == nbrPtg) {
				nbrPtgStr = "Vous avez " + nbrPtg + " pointage à approuver.";
			}else{
				nbrPtgStr = "Vous avez " + nbrPtg + " pointages à approuver.";
			}
			setNombrePointageAApprouver(nbrPtgStr);
		}
	}
	
	@Command
	public void changeEcran(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		div.getChildren().clear();
		Map<String, Div> args = new HashMap<String, Div>();
		args.put("div", div);
		Executions.createComponents(page + ".zul", div, args);
	}
	
	@Command
	public void eaeSharepoint(@BindingParam("page") String page, @BindingParam("ecran") Div div) {
		// if (currentUser.getAgent().getIdAgent() == 9005138 ||
		// currentUser.getAgent().getIdAgent() == 9005131) {
		// Map<String, Div> args = new HashMap<String, Div>();
		// args.put("div", div);
		//
		// div.getChildren().clear();
		// Executions.createComponents(page + ".zul", div, args);
		// } else {
		div.getChildren().clear();
		// redmine #14077 : on redirige vers la page d'accueil
		Map<String, Div> args = new HashMap<String, Div>();
		args.put("div", div);
		Executions.createComponents("accueil.zul", div, args);
		Executions.getCurrent().sendRedirect(sharepointConsumer.getUrlEaeApprobateur(), "_blank");
		// }
	}

	public String getPrenomAgent(String prenom) {
		if (!prenom.equals("")) {
			String premierLettre = prenom.substring(0, 1).toUpperCase();
			String reste = prenom.substring(1, prenom.length()).toLowerCase();
			return premierLettre + reste;
		}
		return "";
	}

	public List<AccueilRhDto> getListeTexteAccueil() {
		return listeTexteAccueil;
	}

	public void setListeTexteAccueil(List<AccueilRhDto> listeTexteAccueil) {
		this.listeTexteAccueil = listeTexteAccueil;
	}

	public ReferentRhDto getRefrentRh() {
		return refrentRh;
	}

	public void setRefrentRh(ReferentRhDto refrentRh) {
		this.refrentRh = refrentRh;
	}

	public boolean isRecette() {
		return isRecette;
	}

	public String getNombreAbsenceAApprouver() {
		return nombreAbsenceAApprouver;
	}

	public void setRecette(boolean isRecette) {
		this.isRecette = isRecette;
	}

	public void setNombreAbsenceAApprouver(String nombreAbsenceAApprouver) {
		this.nombreAbsenceAApprouver = nombreAbsenceAApprouver;
	}

	public String getNombrePointageAApprouver() {
		return nombrePointageAApprouver;
	}

	public void setNombrePointageAApprouver(String nombrePointageAApprouver) {
		this.nombrePointageAApprouver = nombrePointageAApprouver;
	}

	public String getNombreEAEaRealiser() {
		return nombreEAEaRealiser;
	}

	public void setNombreEAEaRealiser(String nombreEAEaRealiser) {
		this.nombreEAEaRealiser = nombreEAEaRealiser;
	}

	public AccessRightsAbsDto getDroitsAbsence() {
		return droitsAbsence;
	}

	public void setDroitsAbsence(AccessRightsAbsDto droitsAbsence) {
		this.droitsAbsence = droitsAbsence;
	}

	public boolean isDroitsEae() {
		return droitsEae;
	}

	public void setDroitsEae(boolean droitsEae) {
		this.droitsEae = droitsEae;
	}

	public AccessRightsPtgDto getDroitsPointage() {
		return droitsPointage;
	}

	public void setDroitsPointage(AccessRightsPtgDto droitsPointage) {
		this.droitsPointage = droitsPointage;
	}

	public String getNombreAbsenceAViser() {
		return nombreAbsenceAViser;
	}

	public void setNombreAbsenceAViser(String nombreAbsenceAViser) {
		this.nombreAbsenceAViser = nombreAbsenceAViser;
	}
	
}
