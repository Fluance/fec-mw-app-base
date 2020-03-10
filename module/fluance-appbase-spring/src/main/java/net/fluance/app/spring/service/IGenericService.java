package net.fluance.app.spring.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.fluance.app.data.model.BaseEntity;


@Service
@Component
@Transactional
public interface IGenericService<T extends BaseEntity, ID extends Serializable> {

    /**
     * Returns the number of entities available.
     * @return
     */
    long    count();
    
    /**
     * Deletes the entity with the given id.
     * @param id
     */
    void    delete(ID id);

    /**
     * Deletes the given entities.
     * @param entities
     */
    void    delete(Iterable<? extends T> entities);

    /**
     * Deletes a given entity.
     * @param entity
     */
    void    delete(T entity);

    /**
     * Deletes all entities managed by the repository.
     */
    void    deleteAll();

    /**
     * Returns whether an entity with the given id exists.
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * Returns all instances of the type.
     * @return
     */
    List<T> findAll();

    /**
     * Returns all instances of the type with the given IDs.
     * @param ids
     * @return
     */
    List<T> findAll(Iterable<ID> ids);

    /**
     * Retrieves an entity by its id.
     * @param id
     * @return
     */
    T   findOne(ID id);

    /**
     * Saves all given entities.
     * @param entities
     * @return
     */
    <S extends T> Iterable<S> save(Iterable<S> entities);
   
    /**
     * Saves a given entity.
     * @param entity
     * @return
     */
    <S extends T> S   save(S entity);
    
    void    deleteAllInBatch();

    void    deleteInBatch(Iterable<T> entities);
        
    T   saveAndFlush(T entity);
    
}