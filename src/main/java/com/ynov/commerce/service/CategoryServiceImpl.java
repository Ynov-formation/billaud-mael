package com.ynov.commerce.service;

import com.ynov.commerce.dao.CategoryRepository;
import com.ynov.commerce.dto.CategoryDto;
import com.ynov.commerce.entities.Category;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }

  @Override
  public List<CategoryDto> getAllCategories() {
    List<CategoryDto> result = new ArrayList<>();
    categoryRepository.findAll()
                      .forEach(category -> result.add(CategoryDto.builder()
                                                                 .id(category.getId())
                                                                 .name(category.getName())
                                                                 .description(category.getDescription())
                                                                 .build()));
    return result;
  }
}
