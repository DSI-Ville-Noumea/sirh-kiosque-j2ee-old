package nc.noumea.mairie.kiosque.export.pdf;

import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Sam
 * 
 */
public interface PdfWriterFactory {
	public PdfWriter getPdfWriter(Document document, OutputStream os) throws Exception;
}