package com.ynov.commerce.service;

import com.ynov.commerce.dto.CategoryDto;
import com.ynov.commerce.entities.Category;

import java.util.List;

public interface CategoryService {
  /**
   * Create a new category
   * @param category the category to create
   * @return the created category
   */
  Category createCategory(Category category);

  /**
   * Update a category
   * @param category the category to update
   * @return the updated category
   */
  Category updateCategory(Category category);

  /**
   * Delete a category
   * @param id the id of the category to delete
   */
  void deleteCategory(Long id);

  /**
   * Get all categories
   * @return the list of all categories
   */
  List<CategoryDto> getAllCategories();
}
