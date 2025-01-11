package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.repository.CategoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, Long> implements CategoryService {
    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

    @Override
    @Cacheable(value = "categoriesAll", key = "'all'")
    public List<Category> getAll() {
        return super.getAll();
    }
}