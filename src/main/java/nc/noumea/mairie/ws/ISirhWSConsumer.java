package nc.noumea.mairie.ws;

import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.profil.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.profil.dto.ServiceTreeDto;

public interface ISirhWSConsumer {

	ProfilAgentDto getEtatCivil(Integer idAgent);

	FichePosteDto getFichePoste(Integer idAgent);

	AgentWithServiceDto getSuperieurHierarchique(Integer idAgent);

	List<ServiceTreeDto> getArbreServiceAgent(Integer idAgent);

}
