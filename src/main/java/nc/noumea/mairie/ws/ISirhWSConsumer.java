package nc.noumea.mairie.ws;

import nc.noumea.mairie.kiosque.profil.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

public interface ISirhWSConsumer {

	ProfilAgentDto getEtatCivil(Integer idAgent);

	FichePosteDto getFichePoste(Integer idAgent);

}
