package nc.noumea.mairie.kiosque.export;

public interface RowRenderer<T, D> {
	/**
	 * Renders the data to the specified row.
	 * 
	 * @param target
	 * @param data
	 * @param oddRow
	 */
	public void render(T target, D data, boolean oddRow);
}
