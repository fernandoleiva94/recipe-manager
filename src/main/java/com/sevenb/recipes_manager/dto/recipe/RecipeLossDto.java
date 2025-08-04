package com.sevenb.recipes_manager.dto.recipe;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecipeLossDto {
    private Long id;
    private Long recipeId;
    private String recipeName;
    private Double quantityLost;
    private String unit;
    private LocalDateTime lossDate;
    private String notes;
    private String imageBase64;
    private String imageUrl;
}

