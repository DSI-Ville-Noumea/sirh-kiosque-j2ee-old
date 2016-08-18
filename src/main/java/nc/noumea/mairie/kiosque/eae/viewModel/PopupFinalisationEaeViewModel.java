package nc.noumea.mairie.kiosque.eae.viewModel;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFinalizationInformationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.ISirhEaeWSConsumer;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PopupFinalisationEaeViewModel {

	private ProfilAgentDto					currentUser;

	private EaeFinalizationInformationDto	finalisationCourant;

	@WireVariable
	private ISirhEaeWSConsumer				eaeWsConsumer;

	@AfterCompose
	public void doAfterCompose(@ExecutionArgParam("eaeCourant") EaeListItemDto eae) {
		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		// on recupere l'EAE selectionné
		EaeFinalizationInformationDto finalisation = eaeWsConsumer.getFinalisationInformation(eae.getIdEae(), currentUser.getAgent().getIdAgent());
		setFinalisationCourant(finalisation);
	}

	public String concatAgent(String nom, String prenom) {
		return nom + " " + prenom;
	}

	public String getEvaluateurs(List<AgentDto> evaluateurs) {
		String res = "";
		for (AgentDto eval : evaluateurs) {
			res += concatAgent(eval.getNom(), eval.getPrenom()) + ", ";
		}
		if (!res.equals("")) {
			res = res.substring(0, res.length() - 2);
		}
		return res;
	}

	public EaeFinalizationInformationDto getFinalisationCourant() {
		return finalisationCourant;
	}

	public void setFinalisationCourant(EaeFinalizationInformationDto finalisationCourant) {
		this.finalisationCourant = finalisationCourant;
	}

}
