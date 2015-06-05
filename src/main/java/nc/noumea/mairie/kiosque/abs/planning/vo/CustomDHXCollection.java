package nc.noumea.mairie.kiosque.abs.planning.vo;

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

import com.dhtmlx.planner.data.DHXCollection;

public class CustomDHXCollection extends DHXCollection {

	public String classNameTr;
	
	public CustomDHXCollection(String value, String label, boolean className) {
		super(value, label);
		if(className) {
			this.classNameTr = "linePair";
		}else{
			this.classNameTr = "lineImpair";
		}
	}

	public String getClassNameTr() {
		return classNameTr;
	}

	public void setClassNameTr(String classNameTr) {
		this.classNameTr = classNameTr;
	}

}
