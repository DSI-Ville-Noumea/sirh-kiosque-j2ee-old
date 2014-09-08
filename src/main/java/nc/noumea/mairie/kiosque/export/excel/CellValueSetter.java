package nc.noumea.mairie.kiosque.export.excel;

import org.zkoss.poi.ss.usermodel.Cell;

/**
 * @author Sam
 * 
 */
public interface CellValueSetter<T> {

	public void setCellValue(T param, Cell cell);
}
