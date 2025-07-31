package com.sevenb.recipes_manager.dto.recipe;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecipeProductionDto {
    private Long id;
    private Long recipeId;
    private String recipeName;
    private Double quantityProduced;
    private String unit;
    private LocalDateTime productionDate;
    private Double expectedQuantity;
    private Double yield;
    private Double cost;
    private String notes;
}

