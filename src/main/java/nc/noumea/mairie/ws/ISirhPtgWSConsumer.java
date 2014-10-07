package nc.noumea.mairie.ws;

import java.util.Date;

import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;

public interface ISirhPtgWSConsumer {

	FichePointageDto getFichePointageSaisie(Integer idAgent, Date date, Integer idAgentConcerne);

	AccessRightsPtgDto getDroitsPointageAgent(Integer idAgent);

}
