package nc.noumea.mairie.ws;

import java.util.List;

import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.kiosque.eae.dto.EaeListItemDto;

public interface ISirhEaeWSConsumer {

	/* TABLEAU BORD */
	List<EaeDashboardItemDto> getTableauBord(Integer idAgent);
	
	/* TABLEAU DES EAEs */

	List<EaeListItemDto> getTableauEae(Integer idAgent);

}
