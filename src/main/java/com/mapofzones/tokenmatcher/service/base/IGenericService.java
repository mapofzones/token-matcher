package com.mapofzones.tokenmatcher.service.base;

import java.util.List;
import java.util.Optional;

public interface IGenericService<T, ID, R extends GenericRepository<T, ID>> {

    Optional<T> findById(ID id);
    List<T> findAll();
    void saveAll(List<T> list);
    T save(T object);

}
