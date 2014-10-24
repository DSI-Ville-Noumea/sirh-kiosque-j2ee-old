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


import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import nc.noumea.mairie.kiosque.export.PdfExporter;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Sam
 * 
 */
public class PdfPCellFactoryImpl implements PdfPCellFactory {
	protected Color defaultBorderColor = WebColors.getRGBColor("#CFCFCF");
	protected Color defaultOddRowBackgroundColor = WebColors.getRGBColor("#F7F7F7");
	protected Color defaultFooterBackgroundColor = WebColors.getRGBColor("#F9F9F9");
	protected JoinCellEvent headerEventJoiner = new JoinCellEventImpl(new PdfPCellEvent() {
		@Override
		public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
//			PdfContentByte pdfContentByte = canvases[PdfPTable.BACKGROUNDCANVAS];
//			URL resource = PdfExporter.class.getResource("/img/menu/conge_up.png");
//			Image image;
//			try {
//				image = Image.getInstance(resource.toString());
//				pdfContentByte.addImage(image, cell.getWidth(), 0, 0, position.getHeight(), position.getLeft(),
//						position.getBottom());
//			} catch (BadElementException e) {
//				throw new RuntimeException(e);
//			} catch (MalformedURLException e) {
//				throw new RuntimeException(e);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			} catch (DocumentException e) {
//				throw new RuntimeException(e);
//			}
		}
	});
	protected JoinCellEvent groupEventJoiner = new JoinCellEventImpl(new PdfPCellEvent() {
		@Override
		public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
			PdfContentByte pdfContentByte = canvases[PdfPTable.BACKGROUNDCANVAS];
			Image image;
			try {
				URL resource = PdfExporter.class.getResource("group_bg.gif");
				image = Image.getInstance(resource.toString());
				pdfContentByte.addImage(image, cell.getWidth(), 0, 0, position.getHeight(), position.getLeft(),
						position.getBottom());
			} catch (BadElementException e) {
				throw new RuntimeException(e);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		}
	});
	protected JoinCellEvent groupfootEventJoiner = new JoinCellEventImpl(new PdfPCellEvent() {
		@Override
		public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
			PdfContentByte pdfContentByte = canvases[PdfPTable.BACKGROUNDCANVAS];
			Image image;
			try {
				URL resource = PdfExporter.class.getResource("groupfoot_bg.gif");
				image = Image.getInstance(resource.toString());
				pdfContentByte.addImage(image, cell.getWidth(), 0, 0, position.getHeight(), position.getLeft(),
						position.getBottom());
			} catch (BadElementException e) {
				throw new RuntimeException(e);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		}
	});

	@Override
	public PdfPCell getHeaderCell() {
		PdfPCell cell = new PdfPCell();
		cell.setPaddingTop(8);
		cell.setPaddingRight(4);
		cell.setPaddingBottom(7);
		cell.setPaddingLeft(6);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderColor(defaultBorderColor);
		cell.setCellEvent((PdfPCellEvent) headerEventJoiner);
		return cell;
	}

	@Override
	public PdfPCell getCell(boolean isOddRow) {
		PdfPCell cell = getDefaultPdfPCell();
		if (isOddRow)
			cell.setBackgroundColor(defaultOddRowBackgroundColor);
		return cell;
	}

	@Override
	public PdfPCell getGroupCell() {
		PdfPCell cell = getDefaultPdfPCell();
		cell.setCellEvent((PdfPCellEvent) groupEventJoiner);
		return cell;
	}

	@Override
	public PdfPCell getGroupfootCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBorderColor(defaultBorderColor);
		cell.setPaddingTop(5);
		cell.setPaddingRight(4);
		cell.setPaddingBottom(5);
		cell.setPaddingLeft(6);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setCellEvent((PdfPCellEvent) groupfootEventJoiner);
		return cell;
	}

	@Override
	public PdfPCell getFooterCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBorderColor(defaultBorderColor);
		cell.setPaddingTop(5);
		cell.setPaddingRight(10);
		cell.setPaddingBottom(5);
		cell.setPaddingLeft(9);
		cell.setBackgroundColor(defaultFooterBackgroundColor);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected PdfPCell getDefaultPdfPCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBorderColor(defaultBorderColor);
		cell.setPaddingTop(4);
		cell.setPaddingRight(4);
		cell.setPaddingBottom(4);
		cell.setPaddingLeft(6);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}
}