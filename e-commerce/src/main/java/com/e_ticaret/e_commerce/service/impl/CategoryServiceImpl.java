package com.e_ticaret.e_commerce.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.CategoryDto;
import com.e_ticaret.e_commerce.entity.Category;
import com.e_ticaret.e_commerce.repository.CategoryRepository;
import com.e_ticaret.e_commerce.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    
}
