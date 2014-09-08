package nc.noumea.mairie.kiosque.export.excel;

/**
 * @author Sam
 * 
 */
public interface CellValueSetterFactory {

	public <T> CellValueSetter<T> getCellValueSetter(Class<T> cls);
}
