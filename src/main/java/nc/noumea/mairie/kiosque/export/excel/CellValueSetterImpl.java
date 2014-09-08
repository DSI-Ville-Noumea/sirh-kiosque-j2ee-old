package nc.noumea.mairie.kiosque.export.excel;

import static nc.noumea.mairie.kiosque.export.Utils.getStringValue;

import java.util.Locale;

import org.zkoss.poi.ss.format.Formatters;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.zk.ui.Component;

/**
 * @author Sam
 * 
 */
public class CellValueSetterImpl implements CellValueSetter<Component> {
	private final Locale _locale;
	private final DateParser _dateParser;

	public CellValueSetterImpl(Locale locale) {
		_locale = locale;
		_dateParser = new DateParser(_locale);
	}

	@Override
	public void setCellValue(Component component, Cell cell) {
		setCellValue(getStringValue(component), cell);
	}

	private void setCellValue(String value, Cell cell) {
		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
			cell.setCellValue(Boolean.valueOf(value));
		} else {
			parseAndSetCellValueToDoubleDateOrString(value, cell);
		}
	}

	private void parseAndSetCellValueToDoubleDateOrString(String txt, Cell cell) {
		final char dot = Formatters.getDecimalSeparator(_locale);
		final char comma = Formatters.getGroupingSeparator(_locale);
		String txt0 = txt;
		if (dot != '.' || comma != ',') {
			final int dotPos = txt.lastIndexOf(dot);
			txt0 = txt.replace(comma, ',');
			if (dotPos >= 0) {
				txt0 = txt0.substring(0, dotPos) + '.' + txt0.substring(dotPos + 1);
			}
		}
		try {
			final Double val = Double.parseDouble(txt0);
			cell.setCellValue(val);
		} catch (NumberFormatException ex) {
			parseAndSetCellValueToDateOrString(txt, cell);
		}
	}

	private void parseAndSetCellValueToDateOrString(String txt, Cell cell) {
		try {
			_dateParser.parseToDate(txt);
			cell.setCellValue(txt);
		} catch (Exception e) {
			cell.setCellValue(txt);
		}
	}
}