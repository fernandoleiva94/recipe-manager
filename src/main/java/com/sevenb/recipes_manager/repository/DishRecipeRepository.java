package com.sevenb.recipes_manager.repository;


import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.entity.DishRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRecipeRepository extends JpaRepository<DishRecipe, Long> {
    List<DishRecipe> findByDish(DishEntity dish);
}
