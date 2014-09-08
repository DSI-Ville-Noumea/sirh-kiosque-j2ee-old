package nc.noumea.mairie.kiosque.export.pdf;

import com.lowagie.text.pdf.PdfPCell;

/**
 * @author Sam
 * 
 */
public interface PdfPCellFactory {
	public PdfPCell getHeaderCell();

	public PdfPCell getCell(boolean isOddRow);

	public PdfPCell getGroupCell();

	public PdfPCell getGroupfootCell();

	public PdfPCell getFooterCell();
}
