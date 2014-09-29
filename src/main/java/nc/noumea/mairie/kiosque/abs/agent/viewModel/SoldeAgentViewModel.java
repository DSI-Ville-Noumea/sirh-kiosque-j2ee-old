package nc.noumea.mairie.kiosque.abs.agent.viewModel;

import java.util.Date;

import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.dto.LightUserDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SoldeAgentViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	private SoldeDto soldeCourant ;

	@Init
	public void initSoldeAgent() {
		
		LightUserDto currentUser = (LightUserDto) Sessions.getCurrent().getAttribute("currentUser");
		
		FiltreSoldeDto filtreDto = new FiltreSoldeDto();
		filtreDto.setDateDebut(new Date());
		filtreDto.setDateFin(new Date());
		SoldeDto result = absWsConsumer.getAgentSolde(currentUser.getEmployeeNumber(), filtreDto);
		setSoldeCourant(result);
	}

	public SoldeDto getSoldeCourant() {
		return soldeCourant;
	}

	public void setSoldeCourant(SoldeDto soldeCourant) {
		this.soldeCourant = soldeCourant;
	}
}
