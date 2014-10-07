package nc.noumea.mairie.ws;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;

public interface ISirhPtgWSConsumer {

	FichePointageDto getFichePointageSaisie(Integer idAgent, Date date, Integer idAgentConcerne);

	/* DROITS */
	AccessRightsPtgDto getDroitsPointageAgent(Integer idAgent);

	/* FILTRES */
	List<ServiceDto> getServicesPointages(Integer idAgent);

}
