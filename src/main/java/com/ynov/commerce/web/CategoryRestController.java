package com.ynov.commerce.web;

import com.ynov.commerce.dto.CategoryDto;
import com.ynov.commerce.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryRestController {
  private final CategoryService categoryService;

  public CategoryRestController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping(value = "/categories")
  public List<CategoryDto> getAllCategories() {
    return categoryService.getAllCategories();
  }
}
