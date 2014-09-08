package nc.noumea.mairie.kiosque.export.pdf;

import com.lowagie.text.Document;

/**
 * @author Sam
 * 
 */
public interface DocumentFactory {
	public Document getDocument();
}
