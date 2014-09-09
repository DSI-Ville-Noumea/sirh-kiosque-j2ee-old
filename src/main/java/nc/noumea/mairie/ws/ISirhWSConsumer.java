package nc.noumea.mairie.ws;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

public interface ISirhWSConsumer {

	ProfilAgentDto getEtatCivil(Integer idAgent) ;

}
