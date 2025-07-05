package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
    List<RecipeCategory> findByUserId(Long userId);
}
