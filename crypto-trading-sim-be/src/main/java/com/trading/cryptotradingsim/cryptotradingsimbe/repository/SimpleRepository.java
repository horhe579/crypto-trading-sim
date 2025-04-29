package com.trading.cryptotradingsim.cryptotradingsimbe.repository;

import java.util.List;
import java.util.Optional;

public interface SimpleRepository<T, ID> {

    T save(T entity);

    T update(T entity);

    Optional<T> getById(ID id);

    List<T> getAll();

    void deleteById(ID id);
}
