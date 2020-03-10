/**
 * 
 */
package net.fluance.app.spring.data.jpa.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import net.fluance.app.spring.data.jpa.model.JPABaseEntity;
import net.fluance.app.spring.data.jpa.repository.IJPAGenericRepository;

public abstract class JPAGenericRepository<T extends JPABaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements IJPAGenericRepository<T, ID> {

	public JPAGenericRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}
	
}
