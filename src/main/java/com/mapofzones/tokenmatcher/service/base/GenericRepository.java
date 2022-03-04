package com.mapofzones.tokenmatcher.service.base;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;

@NoRepositoryBean
public class GenericRepository<T, ID> extends SimpleJpaRepository<T, ID> implements IGenericRepository<T, ID> {

    public GenericRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }
}
