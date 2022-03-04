package com.mapofzones.tokenmatcher.service.base;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class GenericService<T, ID, R extends IGenericRepository<T, ID>> implements IGenericService<T, ID, R> {

    protected final R repository;

    public GenericService(R repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void saveAll(List<T> list) {
        repository.saveAll(list);
    }

    @Override
    public T save(T object) {
        return repository.save(object);
    }
}
