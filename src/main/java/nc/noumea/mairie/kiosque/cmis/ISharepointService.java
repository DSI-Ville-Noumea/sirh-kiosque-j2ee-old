package nc.noumea.mairie.kiosque.cmis;

import java.util.List;

public interface ISharepointService {

	List<SharepointDto> getAllEae(Integer idAgent) throws Exception;
}
