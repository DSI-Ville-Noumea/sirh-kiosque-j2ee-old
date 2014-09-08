package nc.noumea.mairie.kiosque.export.pdf;

import com.lowagie.text.Font;

/**
 * @author Sam
 * 
 */
public interface FontFactory {
	public final static String FONT_TYPE_HEADER = "header";
	public final static String FONT_TYPE_CELL = "cell";
	public final static String FONT_TYPE_GROUP = "group";
	public final static String FONT_TYPE_GROUPFOOT = "groupfoot";
	public final static String FONT_TYPE_FOOTER = "footer";

	public Font getFont(String type);
}