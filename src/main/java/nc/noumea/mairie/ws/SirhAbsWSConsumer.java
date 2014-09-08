package nc.noumea.mairie.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeEtatChangeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatDto;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
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

	private static final String sirhAgentSoldeUrl = "solde/soldeAgent";
	private static final String sirhChangerEtatDemandesAgentUrl = "demandes/changerEtats";
	private static final String sirhDeleteDemandesAgentUrl = "demandes/deleteDemande";
	private static final String sirhSaveDemandesAgentUrl = "demandes/demande";
	private static final String sirhDemandesAgentUrl = "demandes/listeDemandesAgent";
	private static final String sirhTypeAbsenceKiosqueUrl = "filtres/getTypeAbsenceKiosque";
	private static final String sirhEtatAbsenceKiosqueUrl = "filtres/getEtats";
	private static final String sirhGroupeAbsenceUrl = "filtres/getGroupesAbsence";
	private static final String sirhListOrganisationUrl = "organisation/listOrganisationActif";
	private static final String sirhPrintDemandesAgentUrl = "edition/downloadTitreDemande";

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

		String json = new JSONSerializer().exclude("*.class").exclude("*.dureeToString").exclude("*.heureDebut")
				.exclude("*.etat").transform(new MSDateTransformer(), Date.class).deepSerialize(dto);

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
	public List<RefEtatDto> getEtatAbsenceKiosque(String onglet) {
		String url = String.format(sirhAbsWsBaseUrl + sirhEtatAbsenceKiosqueUrl);
		HashMap<String, String> params = new HashMap<>();
		params.put("ongletDemande", onglet);

		ClientResponse res = createAndFireGetRequest(params, url);
		return readResponseAsList(RefEtatDto.class, res, url);
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

}
