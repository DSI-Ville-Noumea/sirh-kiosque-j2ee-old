package nc.noumea.mairie.kiosque.export.pdf;

import com.lowagie.text.pdf.PdfPCellEvent;

/**
 * @author Sam
 * 
 */
public interface JoinCellEvent {
	public void addEvent(PdfPCellEvent event);

	public void removeEvent(PdfPCellEvent event);
}
