package nc.noumea.mairie.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.PointagesEtatChangeDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypePointageDto;
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
	private String sirhPtgWsBaseUrl;

	private static final String ptgFichePointageSaisieUrl = "saisie/fiche";

	/* Gestion des pointages */
	private static final String ptgListePointagesUrl = "visualisation/pointages";
	private static final String ptgChangeEtatPointageUrl = "visualisation/changerEtats";

	/* Filtres */
	private static final String ptgServicesKiosqueUrl = "filtres/services";
	private static final String ptgEtatPointageKiosqueUrl = "filtres/getEtats";
	private static final String ptgTypePointageKiosqueUrl = "filtres/getTypes";
	private static final String ptgAgentsKiosqueUrl = "filtres/agents";

	/* Droits */
	private static final String ptgDroitsDroitsAgentUrl = "droits/listeDroitsAgent";
	private static final String ptgDroitsAgentsApprouvesUrl = "droits/agentsApprouves";
	private static final String ptgDroitsDelegataireOperateursUrl = "droits/delegataireOperateurs";
	private static final String ptgDroitsAgentsSaisisUrl = "droits/agentsSaisis";

	/* Impression des fiches */
	private static final String ptgFichesPointagesUrl = "edition/listeFiches";
	private static final String ptgPrintFichesPointagesUrl = "edition/downloadFichesPointage";

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
	public List<ServiceDto> getServicesPointages(Integer idAgent) {
		String url = String.format(sirhPtgWsBaseUrl + ptgServicesKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(ServiceDto.class, res, url);
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

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(dto);

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

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(listSelect);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
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
		String url = String.format(sirhPtgWsBaseUrl + ptgDroitsAgentsApprouvesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("idOperateur", idOperateur.toString());

		String json = new JSONSerializer().exclude("*.class").exclude("*.civilite").exclude("*.selectedDroitAbs")
				.transform(new MSDateTransformer(), Date.class).deepSerialize(listSelect);

		ClientResponse res = createAndFirePostRequest(params, url, json);
		return readResponse(ReturnMessageDto.class, res, url);
	}

	/**************************** Droits *******************************/
	@Override
	public List<AgentDto> getFichesToPrint(Integer idAgent, String codeService) {
		String url = String.format(sirhPtgWsBaseUrl + ptgFichesPointagesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("codeService", codeService);

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
	public List<AgentDto> getAgentsPointages(Integer idAgent, String codeService) {
		String url = String.format(sirhPtgWsBaseUrl + ptgAgentsKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgent.toString());
		params.put("codeService", codeService);

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(AgentDto.class, res, url);
	}

	@Override
	public List<ConsultPointageDto> getListePointages(Integer idAgentConnecte, Date fromDate, Date toDate,
			String codeService, Integer idAgentRecherche, Integer idEtat, Integer idType) {

		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

		String url = String.format(sirhPtgWsBaseUrl + ptgListePointagesUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("idAgent", idAgentConnecte.toString());
		params.put("codeService", codeService);
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

}
