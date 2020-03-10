/**
 * 
 */
package net.fluance.app.spring.data.jpa.conversion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.core.convert.converter.Converter;

import net.fluance.app.spring.data.jpa.model.JPABaseEntity;

public class StringToBaseEntityConverter<T extends JPABaseEntity> implements
		Converter<String, T> {

	private Class<T> entityType;
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public StringToBaseEntityConverter(Class<T> aEntityClass) {
		Type vType = getClass().getGenericSuperclass();
        ParameterizedType vParameterizedType = (ParameterizedType) vType;
        entityType = (Class<T>) vParameterizedType.getActualTypeArguments()[0];
	}
	
	@Override
	public T convert(String aEntityString) {
		return entityManager.find(entityType, aEntityString);
	}

}
