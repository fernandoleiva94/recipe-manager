package com.sevenb.recipes_manager.dto.recipe;

import lombok.Data;

@Data
public class ProductionRequestDto {
    private Long recipeId;
    private Double quantityProduced;
    private Double expectedQuantity;
    private Double cost;
    private String notes;
}

