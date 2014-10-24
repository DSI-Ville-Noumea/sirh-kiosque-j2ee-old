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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsAbsDto;
import nc.noumea.mairie.kiosque.abs.dto.CompteurDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeEtatChangeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.InputterDto;
import nc.noumea.mairie.kiosque.abs.dto.MotifCompteurDto;
import nc.noumea.mairie.kiosque.abs.dto.MotifDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.ViseursDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.transformer.MSDateTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

import flexjson.JSONSerializer;

@Service("absWsConsumer")
public class SirhAbsWSConsumer extends BaseWsConsumer implements ISirhAbsWSConsumer {

	@Autowired
	@Qualifier("sirhAbsWsBaseUrl")
	private String sirhAbsWsBaseUrl;

	/* Droits */
	private static final String sirhDroitsAgentUrl = "droits/listeDroitsAgent";
	private static final String sirhAgentApprobateurUrl = "droits/agentsApprouves";
	private static final String sirhOperateursDelegataireApprobateurUrl = "droits/inputter";
	private static final String sirhViseursApprobateurUrl = "droits/viseur";
	private static final String sirhAgentsOperatuerOrViseurUrl = "droits/agentsSaisis";

	/* Solde */
	private static final String sirhAgentSoldeUrl = "solde/soldeAgent";

	/* Demandes */
	private static final String sirhChangerEtatDemandesAgentUrl = "demandes/changerEtats";
	private static final String sirhDeleteDemandesAgentUrl = "demandes/deleteDemande";
	private static final String sirhSaveDemandesAgentUrl = "demandes/demande";
	private static final String sirhDemandesAgentUrl = "demandes/listeDemandesAgent";
	private static final String sirhPrintDemandesAgentUrl = "edition/downloadTitreDemande";
	private static final String sirhListeDemandesUrl = "demandes/listeDemandes";
	private static final String sirhListeMotifsRefusUrl = "motif/getListeMotif";

	/* Filtres */
	private static final String sirhTypeAbsenceKiosqueUrl = "filtres/getTypeAbsenceKiosque";
	private static final String sirhEtatAbsenceKiosqueUrl = "filtres/getEtats";
	private static final String sirhGroupeAbsenceUrl = "filtres/getGroupesAbsence";
	private static final String sirhListOrganisationUrl = "organisation/listOrganisationActif";
	private static final String sirhTypeAbsenceCompteurKiosqueUrl = "filtres/getTypeAbsenceCompteurKiosque";
	private static final String sirhServicesKiosqueUrl = "filtres/services";
	private static final String sirhAgentsKiosqueUrl = "filtres/agents";
	private static final String sirhAllTypeAbsenceUrl = "filtres/getAllTypes";

	/* Compteurs */
	private static final String sirhMotifsCompteurKiosqueUrl = "motifCompteur/getListeMotifCompteur";
	private static final String sirhsaveCompteurRecupUrl = "recuperations/addManual";
	private static final String sirhsaveCompteurReposCompUrl = "reposcomps/addManual";

	public SoldeDto getAgentSolde(Integer idAgent, FiltreSoldeDto filtreDto) {

		String url = String.format(sirhAbsWsBaseUrl + sirhAgentSoldeUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(filtreDto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(SoldeDto.class, res, url);
	}

	@Override
	public List<DemandeDto> getDemandesAgent(Integer idAgent, String onglet, Date fromDate, Date toDate,
			Date dateDemande, Integer idRefEtat, Integer idRefType, Integer idRefGroupeAbsence) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

		String url = String.format(sirhAbsWsBaseUrl + sirhDemandesAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("ongletDemande", onglet);
		if (fromDate != null)
			params.put("from", sdf.format(fromDate));
		if (toDate != null)
			params.put("to", sdf.format(toDate));
		if (dateDemande != null)
			params.put("dateDemande", sdf.format(dateDemande));
		if (idRefEtat != null)
			params.put("etat", idRefEtat.toString());
		if (idRefType != null)
			params.put("type", idRefType.toString());
		if (idRefGroupeAbsence != null)
			params.put("groupe", idRefGroupeAbsence.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(DemandeDto.class, res, url);
	}

	@Override
	public List<RefTypeAbsenceDto> getRefTypeAbsenceKiosque(Integer idAgent, Integer idRefGroupeAbsence) {
		String url = String.format(sirhAbsWsBaseUrl + sirhTypeAbsenceKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConcerne", idAgent.toString());
		if (idRefGroupeAbsence != null)
			params.put("idRefGroupeAbsence", idRefGroupeAbsence.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefTypeAbsenceDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveDemandeAbsence(Integer idAgent, DemandeDto dto) {
		String url = String.format(sirhAbsWsBaseUrl + sirhSaveDemandesAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.signature")
				.exclude("*.position").exclude("*.selectedDroitAbs").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(dto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<OrganisationSyndicaleDto> getListOrganisationSyndicale() {
		String url = String.format(sirhAbsWsBaseUrl + sirhListOrganisationUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(OrganisationSyndicaleDto.class, res, url);
	}

	@Override
	public List<RefEtatAbsenceDto> getEtatAbsenceKiosque(String onglet) {
		String url = String.format(sirhAbsWsBaseUrl + sirhEtatAbsenceKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("ongletDemande", onglet);

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefEtatAbsenceDto.class, res, url);
	}

	@Override
	public ReturnMessageDto deleteDemandeAbsence(Integer idAgent, Integer idDemande) {
		String url = String.format(sirhAbsWsBaseUrl + sirhDeleteDemandesAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idDemande", idDemande.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public ReturnMessageDto changerEtatDemandeAbsence(Integer idAgent, DemandeEtatChangeDto dto) {
		String url = String.format(sirhAbsWsBaseUrl + sirhChangerEtatDemandesAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(dto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public byte[] imprimerDemande(Integer idAgent, Integer idDemande) {
		String url = String.format(sirhAbsWsBaseUrl + sirhPrintDemandesAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idDemande", idDemande.toString());
		ClientResponse res = createAndFireRequest(params, url, false, null);

		return readResponseWithFile(res, url);
	}

	@Override
	public List<RefGroupeAbsenceDto> getRefGroupeAbsence() {
		String url = String.format(sirhAbsWsBaseUrl + sirhGroupeAbsenceUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefGroupeAbsenceDto.class, res, url);
	}

	@Override
	public AccessRightsAbsDto getDroitsAbsenceAgent(Integer idAgent) {
		String url = String.format(sirhAbsWsBaseUrl + sirhDroitsAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(AccessRightsAbsDto.class, res, url);
	}

	@Override
	public List<AgentDto> getAgentsApprobateur(Integer idAgent) {
		String url = String.format(sirhAbsWsBaseUrl + sirhAgentApprobateurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveAgentsApprobateur(Integer idAgent, List<AgentDto> listSelect) {
		String url = String.format(sirhAbsWsBaseUrl + sirhAgentApprobateurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(listSelect);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public InputterDto getOperateursDelegataireApprobateur(Integer idAgent) {
		String url = String.format(sirhAbsWsBaseUrl + sirhOperateursDelegataireApprobateurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(InputterDto.class, res, url);
	}

	@Override
	public ViseursDto getViseursApprobateur(Integer idAgent) {
		String url = String.format(sirhAbsWsBaseUrl + sirhViseursApprobateurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(ViseursDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveOperateursDelegataireApprobateur(Integer idAgent, InputterDto dto) {
		String url = String.format(sirhAbsWsBaseUrl + sirhOperateursDelegataireApprobateurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(dto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveViseursApprobateur(Integer idAgent, ViseursDto dto) {
		String url = String.format(sirhAbsWsBaseUrl + sirhViseursApprobateurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(dto);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<AgentDto> getAgentsOperateursOrViseur(Integer idAgentApprobateur, Integer idAgentOperateurOrViseur) {
		String url = String.format(sirhAbsWsBaseUrl + sirhAgentsOperatuerOrViseurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgentApprobateur.toString());
		params.put("idOperateurOrViseur", idAgentOperateurOrViseur.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveAgentsOperateursOrViseur(Integer idAgentApprobateur, Integer idAgentOperateurOrViseur,
			List<AgentDto> listSelect) {
		String url = String.format(sirhAbsWsBaseUrl + sirhAgentsOperatuerOrViseurUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgentApprobateur.toString());
		params.put("idOperateurOrViseur", idAgentOperateurOrViseur.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(listSelect);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<RefTypeAbsenceDto> getRefGroupeAbsenceCompteur() {
		String url = String.format(sirhAbsWsBaseUrl + sirhTypeAbsenceCompteurKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefTypeAbsenceDto.class, res, url);
	}

	@Override
	public List<MotifCompteurDto> getListeMotifsCompteur(Integer idRefTypeAbsence) {
		String url = String.format(sirhAbsWsBaseUrl + sirhMotifsCompteurKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idRefType", idRefTypeAbsence.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(MotifCompteurDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveCompteurRecup(Integer idAgent, CompteurDto compteurACreer) {
		String url = String.format(sirhAbsWsBaseUrl + sirhsaveCompteurRecupUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(compteurACreer);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveCompteurReposComp(Integer idAgent, CompteurDto compteurACreer) {
		String url = String.format(sirhAbsWsBaseUrl + sirhsaveCompteurReposCompUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(compteurACreer);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	@Override
	public List<DemandeDto> getListeDemandes(Integer idAgent, String onglet, Date fromDate, Date toDate,
			Date dateDemande, Integer idRefEtat, Integer idRefType, Integer idAgentRecherche) {

		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

		String url = String.format(sirhAbsWsBaseUrl + sirhListeDemandesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("ongletDemande", onglet);
		if (fromDate != null)
			params.put("from", sdf.format(fromDate));
		if (toDate != null)
			params.put("to", sdf.format(toDate));
		if (dateDemande != null)
			params.put("dateDemande", sdf.format(dateDemande));
		if (idRefEtat != null)
			params.put("etat", idRefEtat.toString());
		if (idRefType != null)
			params.put("type", idRefType.toString());
		if (idAgentRecherche != null)
			params.put("idAgentRecherche", idAgentRecherche.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(DemandeDto.class, res, url);
	}

	@Override
	public List<ServiceDto> getServicesAbsences(Integer idAgent) {
		String url = String.format(sirhAbsWsBaseUrl + sirhServicesKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ServiceDto.class, res, url);
	}

	@Override
	public List<AgentDto> getAgentsAbsences(Integer idAgent, String codeService) {
		String url = String.format(sirhAbsWsBaseUrl + sirhAgentsKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("codeService", codeService);

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public List<RefTypeAbsenceDto> getAllRefTypeAbsence() {
		String url = String.format(sirhAbsWsBaseUrl + sirhAllTypeAbsenceUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefTypeAbsenceDto.class, res, url);
	}

	@Override
	public List<MotifDto> getListeMotifsRefus() {
		String url = String.format(sirhAbsWsBaseUrl + sirhListeMotifsRefusUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(MotifDto.class, res, url);
	}
}
