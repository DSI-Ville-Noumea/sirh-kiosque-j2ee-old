package nc.noumea.mairie.ws;

import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.travail.dto.ServiceTreeDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private static final String sirhEquipeAgentUrl = "agents/equipe";
	private static final String sirhPrintFDPAgentUrl = "fichePostes/downloadFichePoste";

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
	public List<AgentWithServiceDto> getAgentEquipe(Integer idAgent, String sigle) {
		String url = String.format(sirhWsBaseUrl + sirhEquipeAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("sigleService", sigle);

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

}
