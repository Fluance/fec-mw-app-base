package net.fluance.app.data.repository;

import java.io.Serializable;


//@NoRepositoryBean
public interface IGenericRepository<BaseEntity, ID extends Serializable> {
	
	boolean deleteById(ID aId);
	
}