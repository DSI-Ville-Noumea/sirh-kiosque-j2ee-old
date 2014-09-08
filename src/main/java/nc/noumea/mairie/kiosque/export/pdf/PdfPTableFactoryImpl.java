package nc.noumea.mairie.kiosque.export.pdf;

import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Sam
 * 
 */
public class PdfPTableFactoryImpl implements PdfPTableFactory {
	@Override
	public PdfPTable getPdfPTable(int columnSize) {
		PdfPTable table = new PdfPTable(columnSize);
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		return table;
	}
}