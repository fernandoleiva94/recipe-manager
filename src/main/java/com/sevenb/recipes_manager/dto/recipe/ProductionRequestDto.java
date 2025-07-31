package com.sevenb.recipes_manager.dto.recipe;

import lombok.Data;

@Data
public class ProductionRequestDto {
    private Long recipeId;
    private Double quantityProduced;
    private Double cost;
    private String notes;
    private Double recipeQuantity ; // cantidad de receta, por ejemplo : 1.5 veces la receta, 2 veces la receta, 0.5 veces la receta.
}

