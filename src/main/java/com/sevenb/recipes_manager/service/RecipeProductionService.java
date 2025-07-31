package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.dto.recipe.ProductionRequestDto;
import com.sevenb.recipes_manager.dto.recipe.RecipeProductionDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.entity.RecipeProduction;
import com.sevenb.recipes_manager.repository.RecipeProductionRepository;
import com.sevenb.recipes_manager.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecipeProductionService {
    private final RecipeProductionRepository recipeProductionRepository;
    private final RecipeRepository recipeRepository;

    public RecipeProductionService(RecipeProductionRepository recipeProductionRepository, RecipeRepository recipeRepository) {
        this.recipeProductionRepository = recipeProductionRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public RecipeProduction registerProduction(ProductionRequestDto dto, Long userId) {
        Recipe recipe = recipeRepository.findById(dto.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        RecipeProduction production = new RecipeProduction();
        production.setRecipe(recipe);
        production.setQuantityProduced(dto.getQuantityProduced());
        production.setUnit(recipe.getUnit());
        production.setProductionDate(LocalDateTime.now());
        production.setUserId(userId);
        production.setExpectedQuantity(dto.getExpectedQuantity());
        production.setYield(dto.getExpectedQuantity() != null && dto.getExpectedQuantity() > 0 ? dto.getQuantityProduced() / dto.getExpectedQuantity() : null);
        production.setCost(dto.getCost());
        production.setNotes(dto.getNotes());
        return recipeProductionRepository.save(production);
    }

    public List<RecipeProduction> getAllProductionsByUser(Long userId) {
        return recipeProductionRepository.findAllByUserId(userId);
    }

    public RecipeProductionDto toDto(RecipeProduction production) {
        RecipeProductionDto dto = new RecipeProductionDto();
        dto.setId(production.getId());
        dto.setRecipeId(production.getRecipe().getId());
        dto.setRecipeName(production.getRecipe().getName());
        dto.setQuantityProduced(production.getQuantityProduced());
        dto.setUnit(production.getUnit());
        dto.setProductionDate(production.getProductionDate());
        dto.setExpectedQuantity(production.getExpectedQuantity());
        dto.setYield(production.getYield());
        dto.setCost(production.getCost());
        dto.setNotes(production.getNotes());
        return dto;
    }

    public List<RecipeProductionDto> getAllProductionsByUserDto(Long userId) {
        return recipeProductionRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Puedes agregar más métodos para filtrar por receta, usuario, fechas, etc.
}
