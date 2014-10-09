package nc.noumea.mairie.ws;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsDto;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;

public interface ISirhPtgWSConsumer {

	FichePointageDto getFichePointageSaisie(Integer idAgent, Date date, Integer idAgentConcerne);

	/* DROITS */
	AccessRightsPtgDto getDroitsPointageAgent(Integer idAgent);

	/* FILTRES */
	List<ServiceDto> getServicesPointages(Integer idAgent);

	DelegatorAndOperatorsDto getDelegateAndOperator(Integer idAgent);

	List<AgentDto> getApprovedAgents(Integer idAgent);

	ReturnMessageDto saveApprovedAgents(Integer idAgent,
			List<AgentDto> listSelect);

	ReturnMessageDto saveDelegateAndOperator(Integer idAgent,
			DelegatorAndOperatorsDto dto);

	AccessRightsDto getListAccessRightsByAgent(Integer idAgent);

	List<AgentDto> getAgentsSaisisOperateur(Integer idAgent, Integer idOperateur);

	ReturnMessageDto saveAgentsSaisisOperateur(Integer idAgent,
			Integer idOperateur, List<AgentDto> listSelect);

}
