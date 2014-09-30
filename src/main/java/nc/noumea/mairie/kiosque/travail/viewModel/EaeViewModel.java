package nc.noumea.mairie.kiosque.travail.viewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.cmis.SharepointDto;
import nc.noumea.mairie.kiosque.cmis.SharepointService;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EaeViewModel {

	private List<SharepointDto> listeUrlEae;

	private ProfilAgentDto currentUser;

	@Init
	public void iniEaeAgent() throws Exception {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		SharepointService serv = new SharepointService();
		List<SharepointDto> res = serv.getAllEae(currentUser.getAgent().getIdAgent());

		// on tri la liste par année
		Collections.sort(res, new Comparator<SharepointDto>() {
			@Override
			public int compare(SharepointDto o1, SharepointDto o2) {
				return o2.getAnnee().compareTo(o1.getAnnee());
			}

		});
		setListeUrlEae(res);
	}

	@Command
	public void visuEAE(@BindingParam("ref") String url) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, String> args = new HashMap<String, String>();
		args.put("url", url);
		Window win = (Window) Executions.createComponents("/travail/visuEae.zul", null, args);
		win.doModal();

	}

	public List<SharepointDto> getListeUrlEae() {
		return listeUrlEae;
	}

	public void setListeUrlEae(List<SharepointDto> listeUrlEae) {
		this.listeUrlEae = listeUrlEae;
	}
}
