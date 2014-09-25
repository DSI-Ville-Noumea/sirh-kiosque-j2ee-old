package nc.noumea.mairie.ws;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.AccessRightsDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.DemandeEtatChangeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.InputterDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatDto;
import nc.noumea.mairie.kiosque.abs.dto.RefGroupeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.ServiceDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.ViseursDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;

public interface ISirhAbsWSConsumer {
	/* SOLDE */

	SoldeDto getAgentSolde(Integer idAgent, FiltreSoldeDto filtreDto);

	/* FILTRES */

	List<RefTypeAbsenceDto> getRefTypeAbsenceKiosque(Integer idAgent, Integer idRefGroupeAbsence);

	List<RefEtatDto> getEtatAbsenceKiosque(String onglet);

	List<OrganisationSyndicaleDto> getListOrganisationSyndicale();

	List<RefGroupeAbsenceDto> getRefGroupeAbsence();

	/* DEMANDES AGENTS */

	List<DemandeDto> getDemandesAgent(Integer idAgent, String onglet, Date fromDate, Date toDate, Date dateDemande,
			Integer idRefEtat, Integer idRefType, Integer idRefGroupeAbsence);

	ReturnMessageDto saveDemandeAbsence(Integer idAgent, DemandeDto dto);

	ReturnMessageDto deleteDemandeAbsence(Integer idAgent, Integer idDemande);

	ReturnMessageDto changerEtatDemandeAbsence(Integer idAgent, DemandeEtatChangeDto dto);

	byte[] imprimerDemande(Integer idAgent, Integer idDemande);

	/* DROITS */

	AccessRightsDto getDroitsAbsenceAgent(Integer idAgent);

	List<AgentDto> getAgentsApprobateur(Integer idAgent);

	InputterDto getOperateursDelegataireApprobateur(Integer idAgent);

	ViseursDto getViseursApprobateur(Integer idAgent);

	ReturnMessageDto saveAgentsApprobateur(Integer idAgent, List<AgentDto> listSelect);

	ReturnMessageDto saveOperateursDelegataireApprobateur(Integer idAgent, InputterDto dto);

	ReturnMessageDto saveViseursApprobateur(Integer idAgent, ViseursDto dto);

	List<AgentDto> getAgentsOperateursOrViseur(Integer idAgentApprobateur, Integer idAgentOperateurOrViseur);

	ReturnMessageDto saveAgentsOperateursOrViseur(Integer idAgentApprobateur, Integer idAgentOperateurOrViseur,
			List<AgentDto> listSelect);

	/* COMPTEURS */

	List<RefTypeAbsenceDto> getRefGroupeAbsenceCompteur();

	List<ServiceDto> getServicesCompteur(Integer idAgent);

	List<AgentDto> getAgentsCompteur(Integer idAgent, String codeService);

}
