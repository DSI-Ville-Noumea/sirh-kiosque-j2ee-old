package nc.noumea.mairie.ws;

import java.util.List;

import nc.noumea.mairie.kiosque.eae.dto.EaeDashboardItemDto;

public interface ISirhEaeWSConsumer {

	/* TABLEAU BORD */
	List<EaeDashboardItemDto> getTableauBord(Integer idAgent);

}
