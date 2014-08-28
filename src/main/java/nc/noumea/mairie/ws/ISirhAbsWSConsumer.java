package nc.noumea.mairie.ws;

import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;

public interface ISirhAbsWSConsumer {

	SoldeDto getAgentSolde(Integer idAgent, FiltreSoldeDto filtreDto);

	List<DemandeDto> getDemandesAgent(Integer idAgent, String onglet);

	List<RefTypeAbsenceDto> getRefTypeAbsenceKiosque(Integer idAgent);
}
