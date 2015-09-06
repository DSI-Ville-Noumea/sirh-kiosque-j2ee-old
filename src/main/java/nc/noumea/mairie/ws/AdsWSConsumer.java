package nc.noumea.mairie.ws;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
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
import java.util.Map;

import nc.noumea.mairie.ads.dto.EntiteDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;

@Service("adsWsConsumer")
public class AdsWSConsumer extends BaseWsConsumer implements IAdsWSConsumer {

	private Logger logger = LoggerFactory.getLogger(AdsWSConsumer.class);

	@Autowired
	@Qualifier("adsWsBaseUrl")
	private String adsWsBaseUrl;

	private static final String sirhAdsGetEntiteUrl = "api/entite/";
	private static final String sirhAdsGetEntiteWithWildrenUrl = "/withChildren";

	@Override
	public EntiteDto getEntiteWithChildrenByIdEntite(Integer idEntite) {

		if (null == idEntite) {
			return null;
		}

		String url = String.format(adsWsBaseUrl + sirhAdsGetEntiteUrl + idEntite.toString()
				+ sirhAdsGetEntiteWithWildrenUrl);

		Map<String, String> parameters = new HashMap<String, String>();

		ClientResponse res = createAndFireGetRequest(parameters, url);
		try {
			return readResponse(EntiteDto.class, res, url);
		} catch (Exception e) {
			logger.error("L'application ADS ne repond pas." + e.getMessage());
		}

		return null;
	}
}
