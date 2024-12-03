package com.example.expenseapi.service;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;

public class GenericServiceImpl<T, ID> implements GenericService<T, ID> {
    private final CrudRepository<T, ID> repository;

    public GenericServiceImpl(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T get(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<T> getAll() {
        return (List<T>) repository.findAll();
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        T existingEntity = repository.findById(id).orElse(null);
        if (existingEntity != null) {
            BeanUtils.copyProperties(entity, existingEntity, "id");
            return repository.save(existingEntity);
        }
        else {
            return null;
        }
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllData() {
        repository.deleteAll();
    }

}
