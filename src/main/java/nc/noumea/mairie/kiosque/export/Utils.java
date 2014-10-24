package nc.noumea.mairie.kiosque.export;

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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.impl.HeadersElement;
import org.zkoss.zul.impl.MeshElement;

/**
 * @author Sam
 * 
 */
public class Utils {
	private Utils() {
	};

	public static MeshElement getTarget(Component cmp) {
		MeshElement target = (MeshElement) invokeComponentGetter(cmp, "getListbox", "getGrid");
		if (target == null)
			throw new IllegalArgumentException(cmp + " cannot find tagret (MeshElement)");
		return target;
	}

	public static Component getFooters(Component target) {
		// get Grid's foot component or get Listbox's Listfoot component
		return (Component) invokeComponentGetter(target, "getFoot", "getListfoot");
	}

	public static Component getFooterColumnHeader(Component footer) {
		return (Component) invokeComponentGetter(footer, "getColumn", "getListheader");
	}

	public static String getStringValue(Component component) {
		return (String) invokeComponentGetter(component, "getLabel", "getText", "getValue");
	}

	public static String getAlign(Component cmp) {
		return (String) invokeComponentGetter(cmp, "getAlign");
	}

	public static int getHeaderSize(Component target) {
		Component headers = getHeaders(target);
		if (headers != null) {
			return headers.getChildren().size();
		}
		// cannot find head component, guess size by row child side
		List<Component> children = target.getChildren();
		int size = 0;
		for (Component cmp : children) {
			if (cmp instanceof Listitem || cmp instanceof Row) {
				size = Math.max(size, cmp.getChildren().size());
			}
		}
		return size;
	}

	public static Component getHeaders(Component target) {
		for (Component cmp : target.getChildren()) {
			if (cmp instanceof HeadersElement) {
				return cmp;
			}
		}
		return null;
	}

	public static Object invokeComponentGetter(Component target, String... methods) {
		Class<? extends Component> cls = target.getClass();
		for (String methodName : methods) {
			try {
				Method method = cls.getMethod(methodName, null);
				Object ret = method.invoke(target, null);
				if (ret != null)
					return ret;
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
		return null;
	}
}