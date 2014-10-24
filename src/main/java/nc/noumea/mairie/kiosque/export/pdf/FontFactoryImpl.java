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

import com.lowagie.text.Font;
import com.lowagie.text.html.WebColors;

/**
 * @author Sam
 * 
 */
public class FontFactoryImpl implements FontFactory {
	protected float defaultFontSize = 10;
	protected Color defaultFontColor = WebColors.getRGBColor("#636363");

	protected Font getHeaderFont() {
		Font font = getDefaultFont();
		font.setStyle(Font.BOLD);
		return font;
	}

	protected Font getCellFont() {
		return getDefaultFont();
	}

	protected Font getGroupFont() {
		Font font = getDefaultFont();
		font.setStyle(Font.BOLD);
		return font;
	}

	protected Font getGroupfootFont() {
		Font font = getDefaultFont();
		font.setStyle(Font.BOLD);
		return font;
	}

	protected Font getFooterFont() {
		return getDefaultFont();
	}

	protected Font getDefaultFont() {
		Font font = new Font();
		font.setColor(defaultFontColor);
		font.setSize(defaultFontSize);
		return font;
	}

	public Font getFont(String type) {
		if (FontFactory.FONT_TYPE_HEADER.equals(type)) {
			return getHeaderFont();
		} else if (FontFactory.FONT_TYPE_CELL.equals(type)) {
			return getCellFont();
		} else if (FontFactory.FONT_TYPE_GROUP.equals(type)) {
			return getGroupFont();
		} else if (FontFactory.FONT_TYPE_GROUPFOOT.equals(type)) {
			return getGroupfootFont();
		} else if (FontFactory.FONT_TYPE_FOOTER.equals(type)) {
			return getFooterFont();
		}
		return getDefaultFont();
	}
}
