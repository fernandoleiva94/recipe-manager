package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.entity.DishRecipe;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.repository.DishRecipeRepository;
import com.sevenb.recipes_manager.repository.DishRepository;
import com.sevenb.recipes_manager.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service

public class DishRecipeService {

    private final DishRecipeRepository dishRecipeRepository;
    private final DishRepository dishRepository;
    private final RecipeRepository recipeRepository;

    public DishRecipeService(DishRecipeRepository dishRecipeRepository, DishRepository dishRepository, RecipeRepository recipeRepository) {
        this.dishRecipeRepository = dishRecipeRepository;
        this.dishRepository = dishRepository;
        this.recipeRepository = recipeRepository;
    }

    public DishRecipe addRecipeToDish(Long dishId, Long recipeId, Double quantity) {
        DishEntity dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        DishRecipe dishRecipe = new DishRecipe();
        dishRecipe.setDish(dish);
        dishRecipe.setRecipe(recipe);
        dishRecipe.setQuantity(quantity);

        return dishRecipeRepository.save(dishRecipe);
    }

    public void removeRecipeFromDish(Long dishRecipeId) {
        dishRecipeRepository.deleteById(dishRecipeId);
    }
}
