package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.DishDto;
import com.sevenb.recipes_manager.dto.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.RecipeDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.service.CloudinaryService;
import com.sevenb.recipes_manager.service.RecipeService;
import com.sevenb.recipes_manager.service.SupplyService;
import com.sevenb.recipes_manager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {


    private final RecipeService recipeService;
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestHeader("Authorization") String authHeader,
                                               @RequestPart("recipe") RecipeDto recipeDto,
                                               @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDto.getDescription());
        recipe.setName(recipeDto.getName());
        recipe.setQuantity(recipeDto.getQuantity());
        recipe.setUnit(recipeDto.getUnit());

        if (image != null) {
            String url = cloudinaryService.upload(image);
            recipe.setImageUrl(url);
        }


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
    public ResponseEntity<RecipeOuputDto> updateRecipe(@RequestPart("recipe") RecipeDto recipe,
                                                       @PathVariable Long id,
                                                       @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        if (image != null) {
            String url = cloudinaryService.upload(image);
            recipe.setImageUrl(url);
        }
        if (Boolean.TRUE.equals(recipe.getDeleteImage())) {
            recipe.setImageUrl(null);
        }


        RecipeOuputDto savedRecipe = recipeService.updateRecipe(recipe,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

}
