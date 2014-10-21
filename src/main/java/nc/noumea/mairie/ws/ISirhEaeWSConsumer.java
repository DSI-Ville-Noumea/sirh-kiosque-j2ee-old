package nc.noumea.mairie.ws;

import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAppreciationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeResultatDto;

public interface ISirhEaeWSConsumer {

	CampagneEaeDto getCampagneEae();

	/* TABLEAU BORD */
	List<EaeDashboardItemDto> getTableauBord(Integer idAgent);

	/* TABLEAU DES EAEs */

	List<EaeListItemDto> getTableauEae(Integer idAgent);

	byte[] imprimerEAE(Integer idEae);

	ReturnMessageDto initialiseEae(Integer idAgent, Integer idAgentEvalue);

	/* POUR LES ONGLETS */
	EaeIdentificationDto getIdentificationEae(Integer idEae, Integer idAgent);

	ReturnMessageDto saveIdentification(Integer idEae, Integer idAgent, EaeIdentificationDto identification);

	List<EaeFichePosteDto> getListeFichePosteEae(Integer idEae, Integer idAgent);

	EaeResultatDto getResultatEae(Integer idEae, Integer idAgent);

	ReturnMessageDto saveResultat(Integer idEae, Integer idAgent, EaeResultatDto resultat);

	EaeAppreciationDto getAppreciationEae(Integer idEae, Integer idAgent, Integer annee);

	ReturnMessageDto saveAppreciation(Integer idEae, Integer idAgent, EaeAppreciationDto appreciationAnnee);

	EaeEvaluationDto getEvaluationEae(Integer idEae, Integer idAgent);

	ReturnMessageDto saveEvaluation(Integer idEae, Integer idAgent, EaeEvaluationDto evaluation);

	EaeAutoEvaluationDto getAutoEvaluationEae(Integer idEae, Integer idAgent);

	ReturnMessageDto saveAutoEvaluation(Integer idEae, Integer idAgent, EaeAutoEvaluationDto autoEvaluation);

}
