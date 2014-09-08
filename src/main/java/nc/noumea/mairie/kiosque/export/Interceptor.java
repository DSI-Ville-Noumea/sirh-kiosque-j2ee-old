package nc.noumea.mairie.kiosque.export;

public interface Interceptor<T> {
	/**
	 * Called before rendering
	 * 
	 * @param target
	 */
	public void beforeRendering(T target);

	/**
	 * Called after rendering
	 * 
	 * @param target
	 */
	public void afterRendering(T target);
}
