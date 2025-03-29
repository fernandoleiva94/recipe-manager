package com.sevenb.recipes_manager.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RecipeDto {

    private Long id;
    private String name;
    private Long portion;
    private Double weightFinal;
    private Double costTotal;
    private String description;
    private Set<RecipeSupplyDto> recipeSuppliesDto;


}
