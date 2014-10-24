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
