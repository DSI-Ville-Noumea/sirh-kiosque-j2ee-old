package nc.noumea.mairie.kiosque.export;

import java.util.Collection;
/**
* @author Sam
*
*/
public interface GroupRenderer <T, D> extends RowRenderer <T, D>{
/**
* Renders the data to the group
*
* @param target
* @param data
*/
public void renderGroup(T target, Collection<D> data);
/**
* Renders the data to the group foot
*
* @param target
* @param data
*/
public void renderGroupfoot(T target, Collection<D> data);
}
