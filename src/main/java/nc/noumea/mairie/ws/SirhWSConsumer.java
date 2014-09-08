package nc.noumea.mairie.ws;

import java.util.HashMap;

import nc.noumea.mairie.kiosque.profil.dto.EtatCivilDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

@Service("sirhWsConsumer")
public class SirhWSConsumer extends BaseWsConsumer implements ISirhWSConsumer {

	@Autowired
	@Qualifier("sirhWsBaseUrl")
	private String sirhWsBaseUrl;

	private static final String sirhAgentSoldeUrl = "agents/getEtatCivil";

	public EtatCivilDto getEtatCivil(Integer idAgent) {

		String url = String.format(sirhWsBaseUrl + sirhAgentSoldeUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(EtatCivilDto.class, res, url);
	}

}
