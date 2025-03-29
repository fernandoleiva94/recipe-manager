package com.sevenb.recipes_manager.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DishOutpuDto {

    private Long id;

    private String name;
    private String description;
    private Double weight;
    private Double costDish;
    private Double profitMargin;
    private Double price;

    private Set<RecipeDto> recipe;
    private Set<SupplyDto> supplies;

}
