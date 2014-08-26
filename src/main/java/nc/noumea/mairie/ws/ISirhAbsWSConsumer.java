package nc.noumea.mairie.ws;

import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;

public interface ISirhAbsWSConsumer {

	SoldeDto getAgentSolde(Integer idAgent, FiltreSoldeDto filtreDto);
}
