package com.sevenb.recipes_manager.dto.recipe;

import lombok.Data;

import java.util.Set;

@Data
public class RecipeInputDto {

    private Long id;
    private String name;
    private Double quantity;
    private String unit;
    private Double costTotal;
    private String description;
    private String imageUrl;
    private Boolean deleteImage;
    private Long categoryId;
    private Set<RecipeSupplyDto> ingredients;
    private Set<SubRecipeInputDto> recipes;
    private String imageBase64;
    private Long userId;


}
