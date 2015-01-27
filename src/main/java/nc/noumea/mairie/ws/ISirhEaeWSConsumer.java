package nc.noumea.mairie.ws;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.eae.dto.CampagneEaeDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAppreciationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeFichePosteDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeIdentificationDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaePlanActionDto;
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

	EaePlanActionDto getPlanActionEae(Integer idEae, Integer idAgent);

	ReturnMessageDto savePlanAction(Integer idEae, Integer idAgent, EaePlanActionDto planAction);

	EaeEvolutionDto getEvolutionEae(Integer idEae, Integer idAgent);

	ReturnMessageDto saveEvolution(Integer idEae, Integer idAgent, EaeEvolutionDto evolution);

	ReturnMessageDto saveDelegataire(Integer idEae, Integer idAgent, Integer idDelegataire);

}
