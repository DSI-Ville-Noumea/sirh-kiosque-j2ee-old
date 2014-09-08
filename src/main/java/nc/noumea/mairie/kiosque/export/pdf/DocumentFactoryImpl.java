package nc.noumea.mairie.kiosque.export.pdf;

import nc.noumea.mairie.kiosque.export.pdf.DocumentFactory;
import com.lowagie.text.Document;

/**
 * @author Sam
 * 
 */
public class DocumentFactoryImpl implements DocumentFactory {
	@Override
	public Document getDocument() {
		return new Document();
	}
}
