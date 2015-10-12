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

import java.util.Hashtable;
import java.util.regex.*;



public class RGBColor {
	static Hashtable<String, double[]> parsedColors = new Hashtable<String, double[]>();
	static Hashtable<String, double[]> parsedColorsPastel = new Hashtable<String, double[]>();

	public static double[] getColor(String color) {
		if (parsedColors.containsKey(color))
			return (double[]) parsedColors.get(color);
		String original = color;
		color = RGBColor.processColorForm(color);
		double[] result = new double[3];
		String r = color.substring(0, 2);
		String g = color.substring(2, 4);
		String b = color.substring(4, 6);
		result[0] = Integer.parseInt(r, 16)/255.0;
		result[1] = Integer.parseInt(g, 16)/255.0;
		result[2] = Integer.parseInt(b, 16)/255.0;
		parsedColors.put(original, result);
		return result;
	}
	
	public static double[] getColorPastel(String color) {
		if (parsedColorsPastel.containsKey(color))
			return (double[]) parsedColorsPastel.get(color);

		String original = color;
		if(color.toUpperCase().equals("008000")) {
			color = "75BA75";
		}
		if(color.toUpperCase().equals("FF0000")) {
			color = "F95151";
		}
		if(color.toUpperCase().equals("FFA500")) {
			color = "FBC664";
		}
		if(color.toUpperCase().equals("0000FF")) {
			color = "5151FF";
		}
		if(color.toUpperCase().equals("EE82EE")) {
			color = "EDAFED";
		}
		
		color = RGBColor.processColorForm(color);
		double[] result = new double[3];
		String r = color.substring(0, 2);
		String g = color.substring(2, 4);
		String b = color.substring(4, 6);
		result[0] = Integer.parseInt(r, 16)/255.0;
		result[1] = Integer.parseInt(g, 16)/255.0;
		result[2] = Integer.parseInt(b, 16)/255.0;
		parsedColorsPastel.put(original, result);
		return result;
	}

	public static String processColorForm(String color) {
		if (color.equals("transparent")) {
			return "";
		}

		Pattern p1 = Pattern.compile("#[0-9A-Fa-f]{6}");
		Matcher m1 = p1.matcher(color);
		if (m1.matches()) {
			return color.substring(1);
		}

		Pattern p2 = Pattern.compile("[0-9A-Fa-f]{6}");
		Matcher m2 = p2.matcher(color);
		if (m2.matches()) {
			return color;
		}

		Pattern p3 = Pattern.compile("rgb\\s?\\(\\s?(\\d{1,3})\\s?,\\s?(\\d{1,3})\\s?,\\s?(\\d{1,3})\\s?\\)");
		Matcher m3 = p3.matcher(color);

		if (m3.matches()) {
			color = "";
			String r = m3.group(1);
			String g = m3.group(2);
			String b = m3.group(3);
			r = Integer.toHexString(Integer.parseInt(r));
			r = (r.length() == 1) ? "0" + r : r;
			g = Integer.toHexString(Integer.parseInt(g));
			g = (g.length() == 1) ? "0" + g : g;
			b = Integer.toHexString(Integer.parseInt(b));
			b = (b.length() == 1) ? "0" + b : b;
			color = r + g + b;
			return color;
		}
		return "";
	}
}