/**
 * 
 */
package net.fluance.app.spring.data.jpa.util;

import java.util.Collection;
import java.util.Iterator;

import net.fluance.app.spring.data.jpa.model.JPABaseEntity;

public class JPAEntityUtils {

	/**
	 * 
	 * @param aEntitiesCollection
	 * @param aEntityId
	 * @return
	 */
	public static final JPABaseEntity findEntityInCollection(Collection<? extends JPABaseEntity> aEntitiesCollection, Object aEntityId) {

		if(aEntityId == null || aEntitiesCollection == null) {
			throw new IllegalArgumentException("The collection and ID must be both not null!");
		}
		
		JPABaseEntity vEntity = null;
		
		Iterator<? extends JPABaseEntity> vEntityCollectionIterator = aEntitiesCollection.iterator();
		
		while(vEntity==null && vEntityCollectionIterator.hasNext()) {
			JPABaseEntity vTmpEntity = vEntityCollectionIterator.next();
			vEntity = ((vTmpEntity.getId()!=null) && (vTmpEntity.getId().equals(aEntityId))) ? vTmpEntity : vEntity;
		}
		
		return vEntity;
	}
	
}
