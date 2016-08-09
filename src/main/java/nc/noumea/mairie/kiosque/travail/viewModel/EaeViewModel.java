package nc.noumea.mairie.kiosque.travail.viewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import nc.noumea.mairie.kiosque.eae.dto.EaeFinalisationDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EaeViewModel {

	private List<EaeFinalisationDto>	listeUrlEae;

	private ProfilAgentDto				currentUser;

	@WireVariable
	private ISirhEaeWSConsumer			eaeWsConsumer;

	@Init
	public void iniEaeAgent() throws Exception {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		List<EaeFinalisationDto> res = eaeWsConsumer.getEeaControle(currentUser.getAgent().getIdAgent());

		// on ne garde que les 3 dernieres EAEs
		if (res != null && res.size() > 3) {
			setListeUrlEae(res.subList(0, 3));
		} else {
			setListeUrlEae(res);
		}
	}

	@Command
	public void visuEAE(@BindingParam("ref") EaeFinalisationDto eae) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, String> args = new HashMap<String, String>();
		args.put("idDocument", eae.getIdDocument());
		Window win = (Window) Executions.createComponents("/travail/visuEae.zul", null, args);
		win.doModal();
	}

	public List<EaeFinalisationDto> getListeUrlEae() {
		return listeUrlEae;
	}

	public void setListeUrlEae(List<EaeFinalisationDto> listeUrlEae) {
		this.listeUrlEae = listeUrlEae;
	}
}
