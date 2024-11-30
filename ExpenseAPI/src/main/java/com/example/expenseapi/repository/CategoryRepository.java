package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
