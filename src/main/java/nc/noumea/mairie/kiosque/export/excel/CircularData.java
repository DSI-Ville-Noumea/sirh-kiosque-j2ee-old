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


import java.util.Locale;

/**
 * @author Sam
 * 
 */
public class CircularData {
	public static final int UPPER = 2;
	public static final int LOWER = 1;
	public static final int NORMAL = 0;
	private final String[] _data;
	private final Locale _locale;

	/* package */CircularData(String[] dataKey, int type, Locale locale) { // ZSS-69
		_locale = locale;
		_data = type == LOWER ? getDataL(dataKey) : type == UPPER ? getDataU(dataKey) : getDataN(dataKey);
	}

	/* package */String getData(int current) {
		return _data[current];
	}

	/* package */int getIndex(String x) {
		for (int j = 0; j < _data.length; ++j) {
			if (_data[j].equalsIgnoreCase(x)) {
				return j;
			}
		}
		return -1;
	}

	/* package */int getIndexByStartsWith(String x) { // ZSS-67
		for (int j = 0; j < _data.length; ++j) {
			if (_data[j].startsWith(x))
				return j;
		}
		return -1;
	}

	/* package */int getSize() {
		return _data.length;
	}

	private String[] getDataN(String[] dataKey) { // normal. E.g. Sunday,
													// Monday...
	// i18n
		return dataKey;
	}

	private String[] getDataL(String[] dataKey) { // all lowercase. E.g. sunday,
													// monday...
		final String[] weekfn = getDataN(dataKey);
		final String[] weekL = new String[weekfn.length];
		for (int j = 0; j < weekfn.length; ++j) {
			weekL[j] = weekfn[j].toLowerCase(_locale); // ZSS-69
		}
		return weekL;
	}

	private String[] getDataU(String[] dataKey) { // all uppercase. E.g. SUNDAY,
													// MONDAY...
		final String[] weekfn = getDataN(dataKey);
		final String[] weekU = new String[weekfn.length];
		for (int j = 0; j < weekfn.length; ++j) {
			weekU[j] = weekfn[j].toUpperCase(_locale); // ZSS-69
		}
		return weekU;
	}
}
