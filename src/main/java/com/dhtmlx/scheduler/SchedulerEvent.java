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

import java.util.regex.*;


public class SchedulerEvent {
	private int day = 0;
	private int week = 0;
	private int month = 0;
	private int len = 0;
	private double x = 0;
	private double y = 0;
	private double width;
	private double height;
	private String type;
	private String headerText;
	private String headerAgendaText;
	private String text = "";
	private String backgroundColor;
	private String color;
	private int footenote = -1;

	public void parse(Element parent) {
		NodeList n1 = parent.getElementsByTagName("body");
		if ((n1 != null)&&(n1.getLength() > 0)) {
			this.text = ((Element) n1.item(0)).getChildNodes().item(0).getNodeValue();
			if (this.text.compareTo("") == 0) {
				this.text = ((Element) n1.item(0)).getNodeValue();
			}
		}
		n1 = parent.getElementsByTagName("header");
		if ((n1 != null)&&(n1.getLength() > 0)) {
			this.headerText = ((Element) n1.item(0)).getChildNodes().item(0).getNodeValue();
			if (this.headerText.compareTo("") == 0) {
				this.headerText = ((Element) n1.item(0)).getNodeValue();
			}
		}
		n1 = parent.getElementsByTagName("head");
		if ((n1 != null)&&(n1.getLength() > 0)) {
			this.headerAgendaText = ((Element) n1.item(0)).getChildNodes().item(0).getNodeValue();
			if (this.headerAgendaText.compareTo("") == 0) {
				this.headerAgendaText = ((Element) n1.item(0)).getNodeValue();
			}
		}
		String day = parent.getAttribute("day");
		if ((day != null)&&(day.compareTo("undefined") != 0)&&(day.compareTo("") != 0)) {
			this.day = Integer.parseInt(day);
		}
		String week = parent.getAttribute("week");
		if ((week != null)&&(week.compareTo("undefined") != 0)&&(week.compareTo("") != 0)) {
			this.week = Integer.parseInt(week);
		}
		String len = parent.getAttribute("len");
		if ((len != null)&&(len.compareTo("undefined") != 0)&&(len.compareTo("") != 0)) {
			this.len = Integer.parseInt(len);
		}
		
		String month = parent.getAttribute("month");
		if ((month != null)&&(month.compareTo("undefined") != 0)&&(month.compareTo("") != 0)) {
			this.month = Integer.parseInt(month);
		}
		String x = parent.getAttribute("x");
		if ((x != null)&&(x.compareTo("undefined") != 0)&&(x.compareTo("") != 0)&&(x.compareTo("auto") != 0)) {
			this.x = Double.parseDouble(x);
		}
		String y = parent.getAttribute("y");
		if ((y != null)&&(y.compareTo("undefined") != 0)&&(y.compareTo("") != 0)&&(x.compareTo("auto") != 0)) {
			this.y = Double.parseDouble(y);
		}
		String width = parent.getAttribute("width");
		if ((width != null)&&(width.compareTo("undefined") != 0)&&(width.compareTo("") != 0)) {
			this.width = Double.parseDouble(width);
		}
		String height = parent.getAttribute("height");
		if ((height != null)&&(height.compareTo("undefined") != 0)&&(height.compareTo("auto") != 0)&&(height.compareTo("") != 0)) {
			this.height = Double.parseDouble(height);
		}
		this.type = parent.getAttribute("type");
		Element body = (Element) parent.getElementsByTagName("body").item(0);
		if (body != null) {
			this.backgroundColor = body.getAttribute("backgroundColor");
			this.color = body.getAttribute("color");
		} else {
			this.backgroundColor = parent.getAttribute("backgroundColor");
			this.color = parent.getAttribute("color");
		}
	}
	
	public String getText() {
		if (this.text == null)
			this.text = "";
		return this.text;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public int getWeek() {
		return this.week;
	}

	public int getLen() {
		return this.len;
	}

	public int getMonth() {
		return this.month;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public String getType() {
		return this.type;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public String getBackgroundColor() {
		this.backgroundColor = this.processColor(this.backgroundColor);
		return this.backgroundColor;
	}
	
	public String getColor() {
		this.color = this.processColor(this.color);
		return this.color;
	}
	
	public String getHeaderText() {
		return this.headerText;
	}
	
	public String getHeaderAgendaText() {
		return this.headerAgendaText;
	}
	
	private String processColor(String color) {
		Pattern bgColorPattern = Pattern.compile("rgb\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
		Matcher mat = bgColorPattern.matcher(color);
		if (mat.matches() == true) {
			String r = mat.group(1);
			String g = mat.group(2);
			String b = mat.group(3);
			r = String.format("%2x", Integer.parseInt(r, 10));
			g = String.format("%2x", Integer.parseInt(g, 10));
			b = String.format("%2x", Integer.parseInt(b, 10));
			color = r + g + b;
			color = color.replace(' ', '0');
		} else {
			Pattern colorPattern = Pattern.compile("#([0-9a-fA-F]{6})");
			Matcher colorMat = colorPattern.matcher(color);
			if (colorMat.matches() == true) {
				color = colorMat.group(1);
			} else {
				color = "transparent";
			}
		}
		return color;
	}

	public void setFootenote(int footenote) {
		this.footenote = footenote;
	}

	public int getFootenote() {
		return this.footenote;
	}
}
