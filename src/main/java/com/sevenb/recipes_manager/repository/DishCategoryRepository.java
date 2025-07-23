package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.DishCategory;
import com.sevenb.recipes_manager.entity.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  DishCategoryRepository extends JpaRepository<DishCategory, Long> {
    List<DishCategory> findByUserId(Long userId);
}
