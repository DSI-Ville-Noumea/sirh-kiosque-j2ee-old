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


import java.text.DateFormatSymbols;
import java.util.Locale;

import org.zkoss.util.CacheMap;
import org.zkoss.util.Pair;

/**
 * @author Sam
 * 
 */
public class FullMonthData extends CircularData {
	private FullMonthData(String[] data, int type, Locale locale) {
		super(data, type, locale);
	}

	private static final CacheMap _monthData;
	static {
		_monthData = new CacheMap(4);
		_monthData.setLifetime(24 * 60 * 60 * 1000);
	}

	public static FullMonthData getInstance(int type, Locale locale) {
		final Pair key = new Pair(locale, Integer.valueOf(type));
		FullMonthData value = (FullMonthData) _monthData.get(key);
		if (value == null) { // create and cache
			DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);
			if (symbols == null) {
				symbols = DateFormatSymbols.getInstance(Locale.US);
			}
			String[] month13 = symbols.getMonths();
			String[] month12 = new String[12];
			System.arraycopy(month13, 0, month12, 0, 12);
			value = new FullMonthData(month12, type, locale);
			_monthData.put(key, value);
		}
		return value;
	}
}
