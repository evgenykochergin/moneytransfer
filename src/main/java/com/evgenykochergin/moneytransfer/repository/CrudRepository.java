package com.evgenykochergin.moneytransfer.repository;

public interface CrudRepository<T, ID> {

    T find(ID id);

    T save(T entity);

    T update(T entity);

    void delete(ID id);
}
