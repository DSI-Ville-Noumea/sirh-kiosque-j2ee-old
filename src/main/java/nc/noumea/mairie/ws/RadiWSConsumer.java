package nc.noumea.mairie.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.dto.LightUserDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

@Service("radiWSConsumer")
public class RadiWSConsumer extends BaseWsConsumer implements IRadiWSConsumer {

	private Logger logger = LoggerFactory.getLogger(RadiWSConsumer.class);

	@Autowired
	@Qualifier("radiWsBaseUrl")
	private String radiWsBaseUrl;

	private static final String searchAgentRadi = "users";
	
	@Override
	public LightUserDto getAgentCompteADByLogin(String login) {

		String url = String.format(radiWsBaseUrl + searchAgentRadi);

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sAMAccountName", login);
		logger.debug("Call " + url + " with sAMAccountName=" + login);

		ClientResponse res = createAndFireGetRequest(parameters, url);
		List<LightUserDto> list = readResponseAsList(LightUserDto.class, res, url);
		return list.size() == 0 ? null : list.get(0);
	}
}
