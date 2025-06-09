package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.RecipeDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.service.RecipeService;
import com.sevenb.recipes_manager.service.SupplyService;
import com.sevenb.recipes_manager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {


    private final RecipeService recipeService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestHeader("Authorization") String authHeader,@RequestBody RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDto.getDescription());
        recipe.setName(recipeDto.getName());
        recipe.setQuantity(recipeDto.getQuantity());
        recipe.setUnit(recipeDto.getUnit());


        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        recipe.setUserId(userId);

        Recipe savedRecipe = recipeService.createRecipe(recipe, recipeDto.getIngredients());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @GetMapping
    public Set<RecipeOuputDto> getAllRecipes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        return recipeService.getAllRecipes(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeOuputDto> getRecipeById(@PathVariable Long id) {
        RecipeOuputDto recipe = recipeService.getRecipeById(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<RecipeOuputDto> updateRecipe(@RequestBody RecipeDto recipe, @PathVariable Long id) {
        RecipeOuputDto savedRecipe = recipeService.updateRecipe(recipe,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

}
