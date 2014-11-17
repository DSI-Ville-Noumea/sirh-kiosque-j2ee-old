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

import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AccueilRhDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.EstChefDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.travail.dto.ServiceTreeDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

@Service("sirhWsConsumer")
public class SirhWSConsumer extends BaseWsConsumer implements ISirhWSConsumer {

	@Autowired
	@Qualifier("sirhWsBaseUrl")
	private String sirhWsBaseUrl;

	private static final String sirhAgentEtatCivilUrl = "agents/getEtatCivil";
	private static final String sirhAgentFichePosteUrl = "fichePostes/getFichePoste";
	private static final String sirhSuperieurHierarchiqueAgentUrl = "agents/getSuperieurHierarchique";
	private static final String sirhArbreServiceAgentUrl = "agents/serviceArbre";
	private static final String sirhEquipeAgentUrl = "agents/getEquipe";
	private static final String sirhAgentSubordonnesUrl = "agents/agentsSubordonnes";
	private static final String sirhEstChefUrl = "agents/estChef";
	private static final String sirhPrintFDPAgentUrl = "fichePostes/downloadFichePoste";
	private static final String sirhAgentsMairieUrl = "agents/listeAgentsMairie";
	private static final String sirhEstHabiliteEaeUrl = "eaes/estHabiliteEAE";
	private static final String sirhReferentRHUrl = "kiosqueRH/getListeReferentRH";
	private static final String sirhAccueilRHUrl = "kiosqueRH/getListeAccueilRH";
	private static final String sirhAgentUrl = "agents/getAgent";

	public ProfilAgentDto getEtatCivil(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhAgentEtatCivilUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(ProfilAgentDto.class, res, url);
	}

	@Override
	public FichePosteDto getFichePoste(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhAgentFichePosteUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(FichePosteDto.class, res, url);
	}

	@Override
	public AgentWithServiceDto getSuperieurHierarchique(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhSuperieurHierarchiqueAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(AgentWithServiceDto.class, res, url);
	}

	@Override
	public List<ServiceTreeDto> getArbreServiceAgent(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhArbreServiceAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ServiceTreeDto.class, res, url);
	}

	@Override
	public List<AgentWithServiceDto> getAgentEquipe(Integer idAgent, String codeService) {
		String url = String.format(sirhWsBaseUrl + sirhEquipeAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		if (codeService != null)
			params.put("codeService", codeService);

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentWithServiceDto.class, res, url);
	}

	@Override
	public byte[] imprimerFDP(Integer idFichePoste) {
		String url = String.format(sirhWsBaseUrl + sirhPrintFDPAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idFichePoste", idFichePoste.toString());
		ClientResponse res = createAndFireRequest(params, url, false, null);

		return readResponseWithFile(res, url);
	}

	@Override
	public EstChefDto isAgentChef(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhEstChefUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(EstChefDto.class, res, url);
	}

	@Override
	public List<AgentWithServiceDto> getListeAgentsMairie() {
		String url = String.format(sirhWsBaseUrl + sirhAgentsMairieUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentWithServiceDto.class, res, url);
	}

	@Override
	public boolean estHabiliteEAE(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhEstHabiliteEaeUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);

		if (res.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
			return false;
		} else if (res.getStatus() == HttpStatus.OK.value()) {
			return true;
		}
		return false;
	}

	@Override
	public List<ReferentRhDto> getListeReferentRH() {
		String url = String.format(sirhWsBaseUrl + sirhReferentRHUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ReferentRhDto.class, res, url);
	}

	@Override
	public AgentWithServiceDto getAgent(Integer idAgentReferent) {
		String url = String.format(sirhWsBaseUrl + sirhAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgentReferent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(AgentWithServiceDto.class, res, url);
	}

	@Override
	public List<AgentDto> getAgentsSubordonnes(Integer idAgent) {
		String url = String.format(sirhWsBaseUrl + sirhAgentSubordonnesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public List<AccueilRhDto> getListeTexteAccueil() {
		String url = String.format(sirhWsBaseUrl + sirhAccueilRHUrl);
		HashMap<String, String> params = new HashMap<>();

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AccueilRhDto.class, res, url);
	}

}
