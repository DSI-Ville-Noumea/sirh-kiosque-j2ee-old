package nc.noumea.mairie.kiosque.export.pdf;

import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Sam
 * 
 */
public interface PdfPTableFactory {
	public PdfPTable getPdfPTable(int columnSize);
}
