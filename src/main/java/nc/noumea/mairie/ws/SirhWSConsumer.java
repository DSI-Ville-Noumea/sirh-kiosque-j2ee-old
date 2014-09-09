package nc.noumea.mairie.ws;

import java.util.HashMap;

import nc.noumea.mairie.kiosque.profil.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

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

}
