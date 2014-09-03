package nc.noumea.mairie.ws;

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.dto.FiltreSoldeDto;
import nc.noumea.mairie.kiosque.abs.dto.OrganisationSyndicaleDto;
import nc.noumea.mairie.kiosque.abs.dto.RefEtatDto;
import nc.noumea.mairie.kiosque.abs.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.abs.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.abs.dto.SoldeDto;

public interface ISirhAbsWSConsumer {

	SoldeDto getAgentSolde(Integer idAgent, FiltreSoldeDto filtreDto);

	List<DemandeDto> getDemandesAgent(Integer idAgent, String onglet, Date fromDate, Date toDate, Date dateDemande,
			Integer idRefEtat, Integer idRefType);

	ReturnMessageDto saveDemandeAbsence(Integer idAgent, DemandeDto dto);

	ReturnMessageDto deleteDemandeAbsence(Integer idAgent, Integer idDemande);

	List<RefTypeAbsenceDto> getRefTypeAbsenceKiosque(Integer idAgent);

	List<RefEtatDto> getEtatAbsenceKiosque(String onglet);

	List<OrganisationSyndicaleDto> getListOrganisationSyndicale();

}
