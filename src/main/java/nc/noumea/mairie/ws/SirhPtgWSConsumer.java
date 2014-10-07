package nc.noumea.mairie.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

@Service("ptgWsConsumer")
public class SirhPtgWSConsumer extends BaseWsConsumer implements ISirhPtgWSConsumer {

	@Autowired
	@Qualifier("sirhPtgWsBaseUrl")
	private String sirhPtgWsBaseUrl;

	private static final String ptgFichePointageSaisieUrl = "saisie/fiche";

	/* Droits */
	private static final String ptgDroitsAgentUrl = "droits/listeDroitsAgent";

	/* Filtres */
	private static final String ptgServicesKiosqueUrl = "filtres/services";

	@Override
	public FichePointageDto getFichePointageSaisie(Integer idAgent, Date date, Integer idAgentConcerne) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

		String url = String.format(sirhPtgWsBaseUrl + ptgFichePointageSaisieUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("date", sdf.format(date));
		params.put("agent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(FichePointageDto.class, res, url);
	}

	@Override
	public AccessRightsPtgDto getDroitsPointageAgent(Integer idAgent) {
		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponse(AccessRightsPtgDto.class, res, url);
	}

	@Override
	public List<ServiceDto> getServicesPointages(Integer idAgent) {
		String url = String.format(sirhPtgWsBaseUrl + ptgServicesKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ServiceDto.class, res, url);
	}

}
