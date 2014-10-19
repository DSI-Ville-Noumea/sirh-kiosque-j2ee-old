package nc.noumea.mairie.ws;

import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

@Service("eaeWsConsumer")
public class SirhEaeWSConsumer extends BaseWsConsumer implements ISirhEaeWSConsumer {

	@Autowired
	@Qualifier("sirhEaeWsBaseUrl")
	private String sirhEaeWsBaseUrl;

	private static final String eaeTableauBordUrl = "eaes/tableauDeBord";
	private static final String eaeTableauEaeUrl = "eaes/listEaesByAgent";
	private static final String eaeImpressionEaeUrl = "reporting/eae";
	private static final String eaeInitialiserEaeUrl = "eaes/initialiserEae";
	/* Pour les onglets */
	private static final String eaeIdentificationUrl = "evaluation/eaeIdentification";

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

}
