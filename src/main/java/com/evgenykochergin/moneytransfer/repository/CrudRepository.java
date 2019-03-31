package com.evgenykochergin.moneytransfer.repository;

import java.util.Collection;

public interface CrudRepository<T, ID> {

    T find(ID id);

    Collection<T> findAll();

    T save(T entity);

    T update(T entity);

    void delete(ID id);
}
