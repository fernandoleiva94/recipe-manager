package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.entity.RecipeCategory;
import com.sevenb.recipes_manager.repository.RecipeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeCategoryService {

    @Autowired
    private RecipeCategoryRepository repository;

    public List<RecipeCategory> getAllByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public RecipeCategory create(RecipeCategory category) {
        return repository.save(category);
    }

    public RecipeCategory update(Long id, RecipeCategory updated, Long userId) {
        RecipeCategory category = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("No autorizado para modificar esta categoría");
        }

        category.setDescription(updated.getDescription());
        return repository.save(category);
    }

    public void delete(Long id, Long userId) {
        RecipeCategory category = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("No autorizado para eliminar esta categoría");
        }

        repository.deleteById(id);
    }
}
