package com.sevenb.recipes_manager.service;


import com.sevenb.recipes_manager.dto.CategoryDto;
import com.sevenb.recipes_manager.entity.Category;
import com.sevenb.recipes_manager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories(Long userId) {
        return categoryRepository.findAllByUserId(userId).stream()
                .map(category -> new CategoryDto(category.getId(), category.getDescription()))
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        com.sevenb.recipes_manager.entity.Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return new CategoryDto(category.getId(), category.getDescription());
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDTO) {
        if (categoryRepository.findByDescriptionAndUserId(categoryDTO.getDescription(), categoryDTO.getUserId()).isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setDescription(categoryDTO.getDescription());
        category.setUserId(categoryDTO.getUserId());
        category = categoryRepository.save(category);
        return new CategoryDto(category.getId(), category.getDescription());
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setDescription(categoryDTO.getDescription());
        categoryRepository.save(category);

        return new CategoryDto(category.getId(), category.getDescription());
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
