package nc.noumea.mairie.ws;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Mairie de Nouméa
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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import nc.noumea.mairie.alfresco.cmis.CmisUtils;

@Service
public class AlfrescoCMISService implements IAlfrescoCMISService {

	@Autowired
	@Qualifier("alfrescoUrl")
	private String			alfrescoUrl;

	private static String	staticAlfrescoUrl;

	@PostConstruct
	public void init() {
		AlfrescoCMISService.staticAlfrescoUrl = alfrescoUrl;
	}

	/**
	 * exemple de nodeRef :
	 * "workspace://SpacesStore/1a344bd7-6422-45c6-94f7-5640048b20ab" exemple d
	 * URL a retourner :
	 * http://localhost:8080/alfresco/service/api/node/workspace/SpacesStore/418c511a-7c0a-4bb1-95a2-37e5946be726/content
	 * 
	 * @param nodeRef
	 *            String
	 * @return String l URL pour acceder au document directement a alfresco
	 */
	public static String getUrlOfDocument(String idDocument) {

		return CmisUtils.getUrlOfDocument(staticAlfrescoUrl, idDocument);
	}

}
