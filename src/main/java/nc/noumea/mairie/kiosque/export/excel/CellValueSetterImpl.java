package nc.noumea.mairie.kiosque.export.excel;

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