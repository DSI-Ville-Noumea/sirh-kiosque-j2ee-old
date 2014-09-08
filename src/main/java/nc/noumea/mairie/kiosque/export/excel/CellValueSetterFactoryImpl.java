package nc.noumea.mairie.kiosque.export.excel;

import java.util.Locale;

import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;

/**
 * @author Sam
 * 
 */
public class CellValueSetterFactoryImpl implements CellValueSetterFactory {
	@SuppressWarnings("unchecked")
	@Override
	public <T> CellValueSetter<T> getCellValueSetter(Class<T> cls) {
		if (cls.isAssignableFrom(Component.class)) {
			return (CellValueSetter<T>) new CellValueSetterImpl(getLocale());
		}
		return null;
	}

	public Locale getLocale() {
		return Locales.getCurrent();
	}
}
