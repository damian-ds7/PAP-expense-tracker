package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Tag(name = "Category Controller", description = "Controller to manage categories")
public class CategoryController extends GenericController<Category, Long> {
    public CategoryController(CategoryService service) {
        super(service);
    }
}
