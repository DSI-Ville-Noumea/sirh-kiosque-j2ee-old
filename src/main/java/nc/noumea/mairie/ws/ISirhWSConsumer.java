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

import java.util.Date;
import java.util.List;

import nc.noumea.mairie.kiosque.dto.AccueilRhDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.AgentGeneriqueDto;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.EntiteWithAgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.EstChefDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;

public interface ISirhWSConsumer {

	ProfilAgentDto getEtatCivil(Integer idAgent);

	FichePosteDto getFichePoste(Integer idAgent);

	FichePosteDto getFichePosteSecondaire(Integer idAgent);

	AgentWithServiceDto getSuperieurHierarchique(Integer idAgent);

	List<AgentWithServiceDto> getAgentEquipe(Integer idAgent, Integer idService);

	byte[] imprimerFDP(Integer idFichePoste);

	EstChefDto isAgentChef(Integer idAgent);

	List<AgentWithServiceDto> getListeAgentsMairie(Integer idAgentConnecte);

	boolean estHabiliteEAE(Integer idAgent);

	List<ReferentRhDto> getListReferentRH(Integer idAgent);

	List<AgentDto> getAgentsSubordonnes(Integer idAgent);

	List<AccueilRhDto> getListeTexteAccueil();

	List<AgentDto> getListDelegataire(Integer idAgent);

	AgentWithServiceDto getAgentEntite(Integer idAgent, Date date);

	ReturnMessageDto getAlerteRHByAgent(Integer idAgent);

	AgentGeneriqueDto getAffectationAgent(Integer idAgent, Date date);

	List<AgentWithServiceDto> getListeAgentsMairieByIdServiceAds(Integer idServiceAds);

	EntiteWithAgentWithServiceDto getListeEntiteWithAgentWithServiceDtoByIdServiceAds(
			Integer idServiceAds, Integer idAgent,
			List<AgentDto> listAgentsAInclureDansArbre);

}
