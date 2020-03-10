/**
 * 
 */
package net.fluance.app.spring.service.crud.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import net.fluance.app.spring.data.jpa.model.JPABaseEntity;
import net.fluance.app.spring.data.jpa.repository.IJPAGenericRepository;
import net.fluance.app.spring.service.crud.IGenericCrudService;

@Transactional
public class GenericCrudService<T extends JPABaseEntity, ID extends Serializable> implements IGenericCrudService<T, ID> {

    @Autowired
    protected IJPAGenericRepository<T, ID> repository;
    
    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void delete(ID id) {
    	T vEntity = repository.findOne(id);
    	if(vEntity != null) {
    		delete(vEntity);
    	}
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        repository.delete(entities);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean exists(ID id) {
        return repository.exists(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return repository.findAll(ids);
    }

    @Override
    public T findOne(ID id) {
        return repository.findOne(id);
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return repository.save(entities);
    }

    @Override
    public <S extends T> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        repository.delete(entities);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public T saveAndFlush(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public Page<T> findPage(int page, int size) {
        PageRequest request = new PageRequest(page - 1, size);
        return repository.findAll(request);
    }

    @Override
    public Page<T> findPage(int page, int size, Sort.Direction sort) {
        PageRequest request = new PageRequest(page - 1, size, sort);
        return repository.findAll(request);
    }

    @Override
    public Page<T> findPage(int page, int size, Sort.Direction sort, String... properties) {
        PageRequest request = new PageRequest(page - 1, size, sort, properties);
        return repository.findAll(request);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
