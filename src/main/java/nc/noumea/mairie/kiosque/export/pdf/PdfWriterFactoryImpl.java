package nc.noumea.mairie.kiosque.export.pdf;

import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Sam
 * 
 */
public class PdfWriterFactoryImpl implements PdfWriterFactory {
	@Override
	public PdfWriter getPdfWriter(Document document, OutputStream os) throws Exception {
		return PdfWriter.getInstance(document, os);
	}
}
