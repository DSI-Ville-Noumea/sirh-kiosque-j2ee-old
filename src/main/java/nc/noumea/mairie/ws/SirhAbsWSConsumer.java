package nc.noumea.mairie.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
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

	private static final String sirhAgentSoldeUrl = "solde/soldeAgent";
	private static final String sirhDemandesAgentUrl = "demandes/listeDemandesAgent";
	private static final String sirhTypeAbsenceKiosqueUrl = "filtres/getTypeAbsenceKiosque";

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
	public List<DemandeDto> getDemandesAgent(Integer idAgent, String onglet) {

		String url = String.format(sirhAbsWsBaseUrl + sirhDemandesAgentUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("ongletDemande", onglet);

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(DemandeDto.class, res, url);
	}

	@Override
	public List<RefTypeAbsenceDto> getRefTypeAbsenceKiosque(Integer idAgent) {

		String url = String.format(sirhAbsWsBaseUrl + sirhTypeAbsenceKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgentConcerne", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefTypeAbsenceDto.class, res, url);
	}
}
