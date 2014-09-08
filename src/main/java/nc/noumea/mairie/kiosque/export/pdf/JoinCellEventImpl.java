package nc.noumea.mairie.kiosque.export.pdf;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;

/**
 * @author Sam
 * 
 */
public class JoinCellEventImpl implements JoinCellEvent, PdfPCellEvent {
	private LinkedHashSet<PdfPCellEvent> _events = new LinkedHashSet<PdfPCellEvent>();

	public JoinCellEventImpl(PdfPCellEvent... events) {
		for (PdfPCellEvent event : events) {
			_events.add(event);
		}
	}

	@Override
	public void addEvent(PdfPCellEvent event) {
		_events.add(event);
	}

	@Override
	public void removeEvent(PdfPCellEvent event) {
		_events.remove(event);
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		Iterator<PdfPCellEvent> iterator = _events.iterator();
		while (iterator.hasNext()) {
			iterator.next().cellLayout(cell, position, canvases);
		}
	}
}
