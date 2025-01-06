package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String categoryName);
}
