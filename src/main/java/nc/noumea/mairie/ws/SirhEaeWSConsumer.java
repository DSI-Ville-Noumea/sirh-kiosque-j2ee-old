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


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAppreciationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaePlanActionDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeResultatDto;
import nc.noumea.mairie.kiosque.transformer.MSDateTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

import flexjson.JSONSerializer;

@Service("eaeWsConsumer")
public class SirhEaeWSConsumer extends BaseWsConsumer implements ISirhEaeWSConsumer {

	@Autowired
	@Qualifier("sirhEaeWsBaseUrl")
	private String sirhEaeWsBaseUrl;

	private static final String eaeCampagneEaeUrl = "eaes/getEaeCampagneOuverte";
	private static final String eaeTableauBordUrl = "eaes/tableauDeBord";
	private static final String eaeTableauEaeUrl = "eaes/listEaesByAgent";
	private static final String eaeImpressionEaeUrl = "reporting/eae";
	private static final String eaeInitialiserEaeUrl = "eaes/initialiserEae";
	/* Pour les onglets */
	private static final String eaeIdentificationUrl = "evaluation/eaeIdentification";
	private static final String eaeFichePosteUrl = "evaluation/eaeFichePoste";
	private static final String eaeResultatUrl = "evaluation/eaeResultats";
	private static final String eaeAppreciationUrl = "evaluation/eaeAppreciations";
	private static final String eaeEvaluationUrl = "evaluation/eaeEvaluation";
	private static final String eaeAutoEvaluationUrl = "evaluation/eaeAutoEvaluation";
	private static final String eaePlanActionUrl = "evaluation/eaePlanAction";
	private static final String eaeEvolutionUrl = "evaluation/eaeEvolution";

	@Override
	public List<EaeDashboardItemDto> getTableauBord(Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeTableauBordUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(EaeDashboardItemDto.class, res, url);
	}

	@Override
	public List<EaeListItemDto> getTableauEae(Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeTableauEaeUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(EaeListItemDto.class, res, url);
	}

	@Override
	public byte[] imprimerEAE(Integer idEae) {
		String url = String.format(sirhEaeWsBaseUrl + eaeImpressionEaeUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		ClientResponse res = createAndFireRequest(params, url, false, null);

		return readResponseWithFile(res, url);
	}

	@Override
	public ReturnMessageDto initialiseEae(Integer idAgent, Integer idAgentEvalue) {
		String url = String.format(sirhEaeWsBaseUrl + eaeInitialiserEaeUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idEvalue", idAgentEvalue.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans l'initialisation de l'EAE.");
			return dto;
		}
		String result = readResponse(String.class, res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public EaeIdentificationDto getIdentificationEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeIdentificationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaeIdentificationDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveIdentification(Integer idEae, Integer idAgent, EaeIdentificationDto identification) {
		String url = String.format(sirhEaeWsBaseUrl + eaeIdentificationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(identification);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public List<EaeFichePosteDto> getListeFichePosteEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeFichePosteUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(EaeFichePosteDto.class, res, url);
	}

	@Override
	public EaeResultatDto getResultatEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeResultatUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaeResultatDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveResultat(Integer idEae, Integer idAgent, EaeResultatDto resultat) {
		String url = String.format(sirhEaeWsBaseUrl + eaeResultatUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(resultat);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public CampagneEaeDto getCampagneEae() {
		String url = String.format(sirhEaeWsBaseUrl + eaeCampagneEaeUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(CampagneEaeDto.class, res, url);
	}

	@Override
	public EaeAppreciationDto getAppreciationEae(Integer idEae, Integer idAgent, Integer annee) {
		String url = String.format(sirhEaeWsBaseUrl + eaeAppreciationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());
		params.put("annee", annee.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaeAppreciationDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveAppreciation(Integer idEae, Integer idAgent, EaeAppreciationDto appreciationAnnee) {
		String url = String.format(sirhEaeWsBaseUrl + eaeAppreciationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(appreciationAnnee);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public EaeEvaluationDto getEvaluationEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeEvaluationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaeEvaluationDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveEvaluation(Integer idEae, Integer idAgent, EaeEvaluationDto evaluation) {
		String url = String.format(sirhEaeWsBaseUrl + eaeEvaluationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(evaluation);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public EaeAutoEvaluationDto getAutoEvaluationEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeAutoEvaluationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaeAutoEvaluationDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveAutoEvaluation(Integer idEae, Integer idAgent, EaeAutoEvaluationDto autoEvaluation) {
		String url = String.format(sirhEaeWsBaseUrl + eaeAutoEvaluationUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(autoEvaluation);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public EaePlanActionDto getPlanActionEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaePlanActionUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaePlanActionDto.class, res, url);
	}

	@Override
	public ReturnMessageDto savePlanAction(Integer idEae, Integer idAgent, EaePlanActionDto planAction) {
		String url = String.format(sirhEaeWsBaseUrl + eaePlanActionUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(planAction);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

	@Override
	public EaeEvolutionDto getEvolutionEae(Integer idEae, Integer idAgent) {
		String url = String.format(sirhEaeWsBaseUrl + eaeEvolutionUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		return readResponse(EaeEvolutionDto.class, res, url);
	}

	@Override
	public ReturnMessageDto saveEvolution(Integer idEae, Integer idAgent, EaeEvolutionDto evolution) {
		String url = String.format(sirhEaeWsBaseUrl + eaeEvolutionUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idEae", idEae.toString());
		params.put("idAgent", idAgent.toString());

		String json = new JSONSerializer().exclude("*.class").transform(new MSDateTransformer(), Date.class)
				.deepSerialize(evolution);

		ClientResponse res = createAndFirePostRequest(params, url, json);

		ReturnMessageDto dto = new ReturnMessageDto();
		if (res.getStatus() != HttpStatus.OK.value()) {
			dto.getErrors().add("Une erreur est survenue dans la sauvegarde de l'EAE.");
			return dto;
		}
		String result = readResponseAsString(res, url);
		dto.getInfos().add(result);
		return dto;
	}

}
