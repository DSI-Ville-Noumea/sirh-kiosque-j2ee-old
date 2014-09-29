package nc.noumea.mairie.ws;

import nc.noumea.mairie.kiosque.dto.LightUserDto;

public interface IRadiWSConsumer {

	LightUserDto getAgentCompteADByLogin(String login);
}
