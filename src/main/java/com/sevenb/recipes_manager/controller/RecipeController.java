package com.sevenb.recipes_manager.controller;


import com.sevenb.recipes_manager.dto.recipe.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.recipe.RecipeInputDto;
import com.sevenb.recipes_manager.dto.recipe.ProductionRequestDto;
import com.sevenb.recipes_manager.dto.recipe.RecipeProductionDto;

import com.sevenb.recipes_manager.entity.RecipeProduction;
import com.sevenb.recipes_manager.service.CloudinaryService;
import com.sevenb.recipes_manager.service.RecipeService;
import com.sevenb.recipes_manager.service.RecipeProductionService;

import com.sevenb.recipes_manager.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;
    private final RecipeProductionService recipeProductionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeOuputDto> createRecipe(@RequestHeader("Authorization") String authHeader,
                                               @RequestBody RecipeInputDto recipeInputDto) throws IOException {


        if (recipeInputDto.getImageBase64() != null && !recipeInputDto.getImageBase64().isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(recipeInputDto.getImageBase64());
            String url = cloudinaryService.upload(imageBytes);
            recipeInputDto.setImageUrl(url);
        }

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        recipeInputDto.setUserId(userId);

        RecipeOuputDto savedRecipe = recipeService.createRecipe(recipeInputDto);
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
    public ResponseEntity<RecipeOuputDto> updateRecipe(@RequestBody RecipeInputDto recipe,
                                                       @PathVariable Long id) throws IOException {

        if (recipe.getImageBase64() != null && !recipe.getImageBase64().isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(recipe.getImageBase64());
            String url = cloudinaryService.upload(imageBytes);
            recipe.setImageUrl(url);
        }
        if (Boolean.TRUE.equals(recipe.getDeleteImage())) {
            recipe.setImageUrl(null);
        }


        RecipeOuputDto savedRecipe = recipeService.updateRecipe(recipe,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    // --- Endpoints de producción de recetas ---
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/production")
    public ResponseEntity<RecipeProductionDto> registerProduction(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ProductionRequestDto productionRequestDto) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        RecipeProduction production = recipeProductionService.registerProduction(productionRequestDto, userId);
        RecipeProductionDto dto = recipeProductionService.toDto(production);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/production")
    public ResponseEntity<List<RecipeProductionDto>> getAllProductionsByUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        List<RecipeProductionDto> productions = recipeProductionService.getAllProductionsByUserDto(userId);
        return ResponseEntity.ok(productions);
    }

}
