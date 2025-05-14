package com.e_ticaret.e_commerce.service;

import java.util.List;

import com.e_ticaret.e_commerce.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
    void deleteCategory(Long id);
}
