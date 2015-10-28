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

import nc.noumea.mairie.ads.dto.EntiteDto;
import nc.noumea.mairie.kiosque.dto.AgentDto;
import nc.noumea.mairie.kiosque.dto.ReturnMessageDto;
import nc.noumea.mairie.kiosque.ptg.dto.AccessRightsPtgDto;
import nc.noumea.mairie.kiosque.ptg.dto.ConsultPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.DelegatorAndOperatorsDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDtoKiosque;
import nc.noumea.mairie.kiosque.ptg.dto.MotifHeureSupDto;
import nc.noumea.mairie.kiosque.ptg.dto.PointagesEtatChangeDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefEtatPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypeAbsenceDto;
import nc.noumea.mairie.kiosque.ptg.dto.RefTypePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.TitreRepasDemandeDto;

public interface ISirhPtgWSConsumer {

	FichePointageDtoKiosque getFichePointageSaisie(Integer idAgent, Date date, Integer idAgentConcerne);

	/* FILTRES */
	List<EntiteDto> getServicesPointages(Integer idAgent);

	List<RefEtatPointageDto> getEtatPointageKiosque();

	List<RefTypePointageDto> getTypePointageKiosque();

	List<AgentDto> getAgentsPointages(Integer idAgent, Integer idServiceADS);

	/* DROITS */
	DelegatorAndOperatorsDto getDelegateAndOperator(Integer idAgent);

	List<AgentDto> getApprovedAgents(Integer idAgent);

	ReturnMessageDto saveApprovedAgents(Integer idAgent, List<AgentDto> listSelect);

	ReturnMessageDto saveDelegateAndOperator(Integer idAgent, DelegatorAndOperatorsDto dto);

	AccessRightsPtgDto getListAccessRightsByAgent(Integer idAgent);

	List<AgentDto> getAgentsSaisisOperateur(Integer idAgent, Integer idOperateur);

	ReturnMessageDto saveAgentsSaisisOperateur(Integer idAgent, Integer idOperateur, List<AgentDto> listSelect);

	/* IMPRESSION */
	List<AgentDto> getFichesToPrint(Integer idAgent, Integer idServiceADS);

	byte[] imprimerFiches(Integer idAgent, Date dateLundi, List<String> listeIdAgentsToPrint);

	/* GESTION POINTAGES */
	List<ConsultPointageDto> getListePointages(Integer idAgentConnecte, Date fromDate, Date toDate, Integer idServiceADS, Integer idAgentRecherche, Integer idEtat, Integer idType, String typeHS);

	ReturnMessageDto changerEtatPointage(Integer idAgent, List<PointagesEtatChangeDto> listeChangeEtat);

	List<ConsultPointageDto> getHistoriquePointage(Integer idAgent, Integer idPointage);

	/* saisie DES pointages */
	List<RefTypeAbsenceDto> getListeRefTypeAbsence();

	List<MotifHeureSupDto> getListeMotifHsup();

	ReturnMessageDto setFichePointageSaisie(Integer idAgent, FichePointageDtoKiosque fichePointageDto);

	/* TITRE REPAS */
	List<RefEtatPointageDto> getEtatTitreRepasKiosque();

	List<TitreRepasDemandeDto> getListTitreRepas(Integer idAgentConnecte, Date fromDate, Date toDate, Integer idServiceADS, Integer idAgentRecherche, Integer idEtat, Date dateMonth);

	ReturnMessageDto setTitreRepas(Integer idAgentConnecte, List<TitreRepasDemandeDto> listTitreRepas);

}
