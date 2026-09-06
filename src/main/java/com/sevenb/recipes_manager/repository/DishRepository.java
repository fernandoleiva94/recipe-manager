package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.DishEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Long> {
    @EntityGraph(attributePaths = {
        "recipes.recipe.recipeSupplies.supply",
        "recipes.recipe.recipeRecipeRelations.subRecipe"
    })
    List<DishEntity> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {
        "category",
        "supplies.supply.category",
        "recipes.recipe.category",
        "recipes.recipe.recipeSupplies.supply.category",
        "recipes.recipe.recipeRecipeRelations.subRecipe"
    })
    Optional<DishEntity> findWithDetailsById(Long id);
}


