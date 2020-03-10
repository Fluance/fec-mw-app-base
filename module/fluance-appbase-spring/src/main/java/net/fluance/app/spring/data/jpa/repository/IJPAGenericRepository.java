package net.fluance.app.spring.data.jpa.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import net.fluance.app.data.repository.IGenericRepository;
import net.fluance.app.spring.data.jpa.model.JPABaseEntity;


@NoRepositoryBean
public interface IJPAGenericRepository<T extends JPABaseEntity, ID extends Serializable> extends
        JpaRepository<T, ID>, IGenericRepository<JPABaseEntity, Serializable> {    
}