package nc.noumea.mairie.ws;

import nc.noumea.mairie.kiosque.profil.dto.EtatCivilDto;

public interface ISirhWSConsumer {

	EtatCivilDto getEtatCivil(Integer idAgent) ;

}
