package com.sevenb.recipes_manager.dto;

import com.sevenb.recipes_manager.dto.recipe.RecipeInputDto;
import com.sevenb.recipes_manager.entity.DishCategory;
import lombok.Data;

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
    private String imageUrl;

    private Set<RecipeInputDto> recipe;
    private Set<SupplyDto> supplies;
    private DishCategoryDto dishCategory;

}
