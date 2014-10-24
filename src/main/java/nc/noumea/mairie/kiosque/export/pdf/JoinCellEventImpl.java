package nc.noumea.mairie.kiosque.export.pdf;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
