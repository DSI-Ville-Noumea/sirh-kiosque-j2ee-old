package nc.noumea.mairie.kiosque.viewModel;

import java.util.Date;

import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.ws.ISirhAbsWSConsumer;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel {

	@WireVariable
	private ISirhAbsWSConsumer absWsConsumer;

	@Command
	public void soldeAgent() {
		FiltreSoldeDto filtreDto = new FiltreSoldeDto();
		filtreDto.setDateDebut(new Date());
		filtreDto.setDateFin(new Date());
		SoldeDto result = absWsConsumer.getAgentSolde(9005138, filtreDto);
	}
}
