package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, Long> implements CategoryService {
    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

}
