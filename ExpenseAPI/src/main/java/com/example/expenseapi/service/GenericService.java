package com.example.expenseapi.service;

import java.util.List;

public interface GenericService<T, ID> {
    T get(ID id);
    List<T> getAll();
    T save(T entity);
    T update(ID id, T entity);
    void delete(ID id);
    void deleteAllData();
}
