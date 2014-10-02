package nc.noumea.mairie.kiosque.travail.viewModel;

import nc.noumea.mairie.ws.ISharepointConsumer;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class VisuEaeViewModel {

	private String url;

	@WireVariable
	private ISharepointConsumer sharepointConsumer;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("url") String url) {
		setUrl(sharepointConsumer.getUrlDocumentEAE() + url);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
