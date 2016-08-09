package nc.noumea.mairie.ws;

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
