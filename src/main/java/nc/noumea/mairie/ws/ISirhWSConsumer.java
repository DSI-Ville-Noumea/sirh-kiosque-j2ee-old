package nc.noumea.mairie.ws;

import java.util.List;

import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;
import nc.noumea.mairie.kiosque.dto.ReferentRhDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.travail.dto.EstChefDto;
import nc.noumea.mairie.kiosque.travail.dto.FichePosteDto;
import nc.noumea.mairie.kiosque.travail.dto.ServiceTreeDto;

public interface ISirhWSConsumer {

	ProfilAgentDto getEtatCivil(Integer idAgent);

	FichePosteDto getFichePoste(Integer idAgent);

	AgentWithServiceDto getSuperieurHierarchique(Integer idAgent);

	List<ServiceTreeDto> getArbreServiceAgent(Integer idAgent);

	List<AgentWithServiceDto> getAgentEquipe(Integer idAgent, String sigle);

	byte[] imprimerFDP(Integer idFichePoste);

	EstChefDto isAgentChef(Integer idAgent);

	List<AgentWithServiceDto> getListeAgentsMairie();

	boolean estHabiliteEAE(Integer idAgent);

	List<ReferentRhDto> getListeReferentRH();

	AgentWithServiceDto getAgent(Integer idAgentReferent);

}
