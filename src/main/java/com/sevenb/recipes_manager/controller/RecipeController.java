package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.RecipeDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.service.RecipeService;
import com.sevenb.recipes_manager.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private SupplyService supplieService;

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDto.getDescription());
        recipe.setName(recipeDto.getName());
        recipe.setPortion(recipeDto.getPortion());
        recipe.setWeightFinal(recipeDto.getWeightFinal());

        Recipe savedRecipe = recipeService.createRecipe(recipe, recipeDto.getRecipeSuppliesDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @GetMapping
    public Set<RecipeOuputDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (Objects.nonNull(recipe))
            return ResponseEntity.ok(recipe);
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody RecipeDto recipe, @PathVariable Long id) {
        Recipe savedRecipe = recipeService.updateRecipe(recipe,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

}
