package nc.noumea.mairie.kiosque.export.pdf;

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
