package nc.noumea.mairie.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("sharepointConsumer")
public class SharepointConsumer extends BaseWsConsumer implements ISharepointConsumer {

	@Autowired
	@Qualifier("sharepointBaseUrl")
	private String sharepointBaseUrl;

	@Override
	public String getUrlEaeApprobateur() {
		return sharepointBaseUrl + "kiosque-rh/_layouts/Noumea.RH.Eae/EAEList.aspx";
	}

	@Override
	public String getUrlTableauBordApprobateur() {
		return sharepointBaseUrl + "kiosque-rh/_layouts/Noumea.RH.Eae/EAETableauDeBord.aspx";
	}

	@Override
	public String getUrlDocumentEAE() {
		return sharepointBaseUrl + "kiosque-rh/EAE/";
	}

}
