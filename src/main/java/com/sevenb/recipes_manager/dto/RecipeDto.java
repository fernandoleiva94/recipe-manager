package com.sevenb.recipes_manager.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RecipeDto {

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
    private Long userId;


}
