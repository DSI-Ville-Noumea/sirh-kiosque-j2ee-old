package nc.noumea.mairie.ws;

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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.ads.dto.EntiteDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.ptg.dto.DpmIndemniteAnneeDto;
import nc.noumea.mairie.kiosque.ptg.dto.DpmIndemniteChoixAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.MotifHeureSupDto;
import nc.noumea.mairie.kiosque.ptg.dto.PointagesEtatChangeDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.TitreRepasDemandeDto;
import nc.noumea.mairie.kiosque.transformer.MSDateTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

import flexjson.JSONSerializer;

@Service("ptgWsConsumer")
public class SirhPtgWSConsumer extends BaseWsConsumer implements ISirhPtgWSConsumer {

	@Autowired
	@Qualifier("sirhPtgWsBaseUrl")
	public String sirhPtgWsBaseUrl;

	private static final String ptgFichePointageSaisieUrl = "saisie/fiche";

	/* Gestion des pointages */
	private static final String ptgListePointagesUrl = "visualisation/pointages";
	private static final String ptgChangeEtatPointageUrl = "visualisation/changerEtats";
	private static final String ptgHistoriquePointageUrl = "visualisation/historique";

	/* Filtres */
	private static final String ptgServicesKiosqueUrl = "filtres/services";
	private static final String ptgservicesWithPrimeDpmKiosqueUrl = "filtres/servicesWithPrimeDpm";
	private static final String ptgEtatPointageKiosqueUrl = "filtres/getEtats";
	private static final String ptgTypePointageKiosqueUrl = "filtres/getTypes";
	private static final String ptgAgentsKiosqueUrl = "filtres/agents";
	private static final String ptgAgentsWithPrimeDpmKiosqueUrl = "filtres/agentsWithPrimeDpm";
	private static final String sirhPtgTypeAbsence = "filtres/getTypesAbsence";
	private static final String sirhPtgMotifHsup = "filtres/getMotifHsup";

	/* Droits */
	private static final String ptgDroitsDroitsAgentUrl = "droits/listeDroitsAgent";
	private static final String ptgDroitsAgentsApprouvesUrl = "droits/agentsApprouves";
	private static final String ptgDroitsDelegataireOperateursUrl = "droits/delegataireOperateurs";
	private static final String ptgDroitsAgentsSaisisUrl = "droits/agentsSaisis";

	/* Impression des fiches */
	private static final String ptgFichesPointagesUrl = "edition/listeFiches";
	private static final String ptgPrintFichesPointagesUrl = "edition/downloadFichesPointage";

	/* Titre repas */
	private static final String ptgEtatTitreRepasKiosqueUrl = "titreRepas/getEtats";
	private static final String ptgListeTitreRepasUrl = "titreRepas/listTitreRepas";
	private static final String ptgSaveTitreRepasUrl = "titreRepas/enregistreListTitreDemande";
	private static final String ptgHistoriqueTitreRepasUrl = "titreRepas/historique";
	
	/* choix prime DPM #30544 */
	private static final String ptgSaveIndemniteChoixAgentUrl = "dpm/saveIndemniteChoixAgent";
	private static final String ptgSaveListIndemniteChoixAgentForOperatorUrl = "dpm/saveListIndemniteChoixAgentForOperator";
	private static final String ptgIndemniteChoixAgentUrl = "dpm/indemniteChoixAgent";
	private static final String ptgListDpmIndemniteChoixAgentUrl = "dpm/listDpmIndemniteChoixAgent";
	private static final String ptgIsPeriodeChoixOuverteUrl = "dpm/isPeriodeChoixOuverte";
	private static final String ptgListDpmIndemAnneeOuverteUrl = "dpm/listDpmIndemAnneeOuverte";
	private static final String ptgDpmIndemAnneeEnCoursUrl = "dpm/dpmIndemAnneeEnCours";
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@Override
	public FichePointageDtoKiosque getFichePointageSaisie(Integer idAgent, Date date, Integer idAgentConcerne) {

		String url = String.format(sirhPtgWsBaseUrl + ptgFichePointageSaisieUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("date", sdf.format(date));
		params.put("agent", idAgentConcerne.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(FichePointageDtoKiosque.class, res, url);
	}

	@Override
	public ReturnMessageDto setFichePointageSaisie(Integer idAgent, FichePointageDtoKiosque fichePointageDto) {

		String url = String.format(sirhPtgWsBaseUrl + ptgFichePointageSaisieUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class).deepSerialize(fichePointageDto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	/**************************** Droits *******************************/
	@Override
	public AccessRightsPtgDto getListAccessRightsByAgent(Integer idAgent) {

		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsDroitsAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(AccessRightsPtgDto.class, res, url);
	}

	@Override
	public List<EntiteDto> getServicesPointages(Integer idAgent) {
		String url = String.format(sirhPtgWsBaseUrl + ptgServicesKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(EntiteDto.class, res, url);
	}

	@Override
	public List<EntiteDto> getServicesWithPrimeDpmPointages(Integer idAgent) {
		String url = String.format(sirhPtgWsBaseUrl + ptgservicesWithPrimeDpmKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(EntiteDto.class, res, url);
	}

	@Override
	public DelegatorAndOperatorsDto getDelegateAndOperator(Integer idAgent) {

		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsDelegataireOperateursUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(DelegatorAndOperatorsDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveDelegateAndOperator(Integer idAgent, DelegatorAndOperatorsDto dto) {
		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsDelegataireOperateursUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs").transform(new MSDateTransformer(), Date.class).deepSerialize(dto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<AgentDto> getApprovedAgents(Integer idAgent) {

		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsAgentsApprouvesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveApprovedAgents(Integer idAgent, List<AgentDto> listSelect) {
		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsAgentsApprouvesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs").transform(new MSDateTransformer(), Date.class).deepSerialize(listSelect);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		try {
			readResponse(res, url);
		} catch (WSConsumerException e) {
			dto.setErrors(Arrays.asList("Une erreur est survenue lors de la sauvegarde des agents à approuver."));
		}

		return dto;
	}

	@Override
	public List<AgentDto> getAgentsSaisisOperateur(Integer idAgent, Integer idOperateur) {

		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsAgentsSaisisUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idOperateur", idOperateur.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveAgentsSaisisOperateur(Integer idAgent, Integer idOperateur, List<AgentDto> listSelect) {
		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsAgentsSaisisUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idOperateur", idOperateur.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs").transform(new MSDateTransformer(), Date.class).deepSerialize(listSelect);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		ReturnMessageDto dto = new ReturnMessageDto();
		try {
			readResponse(res, url);
		} catch (WSConsumerException e) {
			dto.setErrors(Arrays.asList("Une erreur est survenue lors de la sauvegarde des agents à approuver."));
		}

		return dto;
	}

	/**************************** Droits *******************************/
	@Override
	public List<AgentDto> getFichesToPrint(Integer idAgent, Integer idServiceADS) {
		String url = String.format(sirhPtgWsBaseUrl + ptgFichesPointagesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idServiceADS", idServiceADS.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public byte[] imprimerFiches(Integer idAgent, Date dateLundi, List<String> listeIdAgentsToPrint) {
		String url = String.format(sirhPtgWsBaseUrl + ptgPrintFichesPointagesUrl);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String csvIdAgent = "";
		for (String id : listeIdAgentsToPrint) {
			csvIdAgent += id + ",";
		}
		if (!csvIdAgent.equals("")) {
			csvIdAgent = csvIdAgent.substring(0, csvIdAgent.length() - 1);
		}
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("date", sdf.format(dateLundi));
		params.put("csvIdAgents", csvIdAgent);

		ClientResponse res = createAndFireRequest(params, url, false, null);

		return readResponseWithFile(res, url);
	}

	@Override
	public List<RefEtatPointageDto> getEtatPointageKiosque() {
		String url = String.format(sirhPtgWsBaseUrl + ptgEtatPointageKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefEtatPointageDto.class, res, url);
	}

	@Override
	public List<RefTypePointageDto> getTypePointageKiosque() {
		String url = String.format(sirhPtgWsBaseUrl + ptgTypePointageKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefTypePointageDto.class, res, url);
	}

	@Override
	public List<AgentDto> getAgentsPointages(Integer idAgent, Integer idServiceADS) {
		String url = String.format(sirhPtgWsBaseUrl + ptgAgentsKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		if (null != idServiceADS) {
			params.put("idServiceADS", idServiceADS.toString());
		}

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public List<AgentDto> getAgentsPointagesWithPrimeDpm(Integer idAgent, Integer idServiceADS) {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgAgentsWithPrimeDpmKiosqueUrl);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		if (null != idServiceADS) {
			params.put("idServiceADS", idServiceADS.toString());
		}

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}
	
	@Override
	public List<ConsultPointageDto> getListePointages(Integer idAgentConnecte, Date fromDate, Date toDate, Integer idServiceADS, Integer idAgentRecherche, Integer idEtat, Integer idType, String typeHS) {

		String url = String.format(sirhPtgWsBaseUrl + ptgListePointagesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgentConnecte.toString());
		if (null != idServiceADS) {
			params.put("idServiceADS", idServiceADS.toString());
		}
		if (fromDate != null)
			params.put("from", sdf.format(fromDate));
		if (toDate != null)
			params.put("to", sdf.format(toDate));
		if (idEtat != null)
			params.put("etat", idEtat.toString());
		if (idType != null)
			params.put("type", idType.toString());
		if (idAgentRecherche != null)
			params.put("agent", idAgentRecherche.toString());
		if (typeHS != null) {
			if (typeHS.equals("Récupérées")) {
				typeHS = "R";
			} else if (typeHS.equals("Rappel en service")) {
				typeHS = "RS";
			} else if (typeHS.equals("Payées")) {
				typeHS = "AUTRE";
			}
			params.put("typeHS", typeHS);
		}

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ConsultPointageDto.class, res, url);
	}

	@Override
	public ReturnMessageDto changerEtatPointage(Integer idAgent, List<PointagesEtatChangeDto> listeChangeEtat) {
		String url = String.format(sirhPtgWsBaseUrl + ptgChangeEtatPointageUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").deepSerialize(listeChangeEtat);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<ConsultPointageDto> getHistoriquePointage(Integer idAgent, Integer idPointage) {
		String url = String.format(sirhPtgWsBaseUrl + ptgHistoriquePointageUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idPointage", idPointage.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ConsultPointageDto.class, res, url);
	}

	@Override
	public List<RefTypeAbsenceDto> getListeRefTypeAbsence() {
		String url = String.format(sirhPtgWsBaseUrl + sirhPtgTypeAbsence);
		ClientResponse res = createAndFireGetRequest(new HashMap<String, String>(), url);
		return readResponseAsList(RefTypeAbsenceDto.class, res, url);
	}

	@Override
	public List<MotifHeureSupDto> getListeMotifHsup() {
		String url = String.format(sirhPtgWsBaseUrl + sirhPtgMotifHsup);
		ClientResponse res = createAndFireGetRequest(new HashMap<String, String>(), url);
		return readResponseAsList(MotifHeureSupDto.class, res, url);
	}

	@Override
	public List<RefEtatPointageDto> getEtatTitreRepasKiosque() {
		String url = String.format(sirhPtgWsBaseUrl + ptgEtatTitreRepasKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefEtatPointageDto.class, res, url);
	}

	@Override
	public List<TitreRepasDemandeDto> getListTitreRepas(Integer idAgentConnecte, Date fromDate, Date toDate, Integer idServiceADS,
			Integer idAgentRecherche, Integer idEtat, Date dateMonth, Boolean commande) {

		String url = String.format(sirhPtgWsBaseUrl + ptgListeTitreRepasUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConnecte", idAgentConnecte.toString());
		if (null != idServiceADS) {
			params.put("idServiceADS", idServiceADS.toString());
		}
		if (fromDate != null)
			params.put("fromDate", sdf.format(fromDate));
		if (toDate != null)
			params.put("toDate", sdf.format(toDate));
		if (idEtat != null)
			params.put("etat", idEtat.toString());
		if (dateMonth != null)
			params.put("dateMonth", sdf.format(dateMonth));
		if (idAgentRecherche != null)
			params.put("idAgent", idAgentRecherche.toString());
		if (commande != null)
			params.put("commande", commande.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(TitreRepasDemandeDto.class, res, url);
	}

	@Override
	public ReturnMessageDto setTitreRepas(Integer idAgentConnecte, List<TitreRepasDemandeDto> listTitreRepas) {
		String url = String.format(sirhPtgWsBaseUrl + ptgSaveTitreRepasUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConnecte", idAgentConnecte.toString());
		params.put("isFromSIRH", "false");

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class).deepSerialize(listTitreRepas);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<TitreRepasDemandeDto> getHistoriqueTitreRepas(Integer idTrDemande) {
		String url = String.format(sirhPtgWsBaseUrl + ptgHistoriqueTitreRepasUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idTrDemande", idTrDemande.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(TitreRepasDemandeDto.class, res, url);
	}

	@Override
	public boolean isPeriodeChoixOuverte(Integer annee) {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgIsPeriodeChoixOuverteUrl);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("annee", annee.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(boolean.class, res, url);
	}

	@Override
	public DpmIndemniteChoixAgentDto getIndemniteChoixAgent(Integer idAgentConnecte, Integer annee) {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgIndemniteChoixAgentUrl);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConnecte", idAgentConnecte.toString());
		params.put("annee", annee.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(DpmIndemniteChoixAgentDto.class, res, url);
	}

	@Override
	public List<DpmIndemniteChoixAgentDto> getListDpmIndemniteChoixAgent(Integer idAgentConnecte, Integer annee, Integer idServiceAds, Integer idAgentFiltre) {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgListDpmIndemniteChoixAgentUrl);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConnecte", idAgentConnecte.toString());
		params.put("annee", annee.toString());
		
		if(null != idServiceAds)
			params.put("idServiceAds", idServiceAds.toString());

		if(null != idAgentFiltre)
			params.put("idAgentFiltre", idAgentFiltre.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(DpmIndemniteChoixAgentDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveListIndemniteChoixAgentForOperator(Integer idAgentConnecte, Integer annee, List<DpmIndemniteChoixAgentDto> listDto) {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgSaveListIndemniteChoixAgentForOperatorUrl);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConnecte", idAgentConnecte.toString());
		params.put("annee", annee.toString());

		String json = new JSONSerializer()
				.exclude("*.class").exclude("*.radioButtonZK")
				.exclude("*.civilite").exclude("*.selectedDroitAbs").exclude("*.position")
				.exclude("*.signature").exclude("*.statut")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(listDto);
		
		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveIndemniteChoixAgent(Integer idAgentConnecte, DpmIndemniteChoixAgentDto dto) {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgSaveIndemniteChoixAgentUrl);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConnecte", idAgentConnecte.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("radioButtonZK").transform(new MSDateTransformer(), Date.class).deepSerialize(dto);
		
		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}
	
	@Override
	public List<DpmIndemniteAnneeDto> getListDpmIndemAnneeOuverte() {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgListDpmIndemAnneeOuverteUrl);
		
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(DpmIndemniteAnneeDto.class, res, url);
	}
	
	@Override
	public DpmIndemniteAnneeDto getDpmIndemAnneeEnCours() {
		
		String url = String.format(sirhPtgWsBaseUrl + ptgDpmIndemAnneeEnCoursUrl);
		
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(DpmIndemniteAnneeDto.class, res, url);
	}

}
