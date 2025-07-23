package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.entity.DishCategory;
import com.sevenb.recipes_manager.repository.DishCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishCategoryService {

    @Autowired
    private DishCategoryRepository repository;

    public List<DishCategory> getAllByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public DishCategory create(DishCategory category) {
        return repository.save(category);
    }

    public DishCategory update(Long id, DishCategory updated, Long userId) {
        DishCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("No autorizado para modificar esta categoría");
        }

        category.setDescription(updated.getDescription());
        return repository.save(category);
    }

    public void delete(Long id, Long userId) {
        DishCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("No autorizado para eliminar esta categoría");
        }

        repository.deleteById(id);
    }

}
