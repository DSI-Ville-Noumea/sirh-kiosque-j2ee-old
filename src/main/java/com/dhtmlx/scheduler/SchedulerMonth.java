package com.dhtmlx.scheduler;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SchedulerMonth {
	private String monthName;
	private String[][] rows;

	public void parse(Element parent) {
		this.monthName = parent.getAttribute("label");
		NodeList rows = parent.getElementsByTagName("row");
		NodeList cols = parent.getElementsByTagName("column");
		if ((rows != null)&&(cols != null)) {
			this.rows= new String[rows.getLength() + 1][7];
			for (int i = 0; i < cols.getLength(); i++) {
				this.rows[0][i] = cols.item(i).getChildNodes().item(0).getNodeValue();
			}
			for (int i = 1; i <= rows.getLength(); i++) {
				this.rows[i] = rows.item(i - 1).getChildNodes().item(0).getNodeValue().split("\\|");
			}
		}
	}
	
	public String getLabel() {
		return this.monthName;
	}
	
	public String[][] getRows() {
		return this.rows;
	}
	
	public String[][] getOnlyDays() {
		String[][] days = new String[this.rows.length - 1][];
		for (int i = 1; i < this.rows.length; i++) {
			days[i - 1] = this.rows[i];
		}
		return days;
	}
	
}
