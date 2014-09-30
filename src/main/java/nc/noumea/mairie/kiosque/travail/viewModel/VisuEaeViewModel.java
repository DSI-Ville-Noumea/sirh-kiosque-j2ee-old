package nc.noumea.mairie.kiosque.travail.viewModel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class VisuEaeViewModel {

	private String url;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("url") String url) {
		setUrl("http://svq-sp/kiosque-rh/EAE/" + url);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
