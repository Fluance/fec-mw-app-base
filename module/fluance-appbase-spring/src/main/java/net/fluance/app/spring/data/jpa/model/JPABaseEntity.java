/**
 * 
 */
package net.fluance.app.spring.data.jpa.model;

import javax.persistence.MappedSuperclass;

import net.fluance.app.data.model.BaseEntity;


@SuppressWarnings("serial")
@MappedSuperclass
public abstract class JPABaseEntity implements BaseEntity {

}
