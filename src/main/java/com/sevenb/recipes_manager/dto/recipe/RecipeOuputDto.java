package com.sevenb.recipes_manager.dto.recipe;

import com.sevenb.recipes_manager.dto.SupplyDto;
import com.sevenb.recipes_manager.entity.RecipeCategory;
import lombok.Data;

import java.util.Set;

@Data
public class RecipeOuputDto {

    private Long id;
    //no tiene que ser null.
    private String name;
    private String unit;
    private Double quantity;
    private String description;
    private Double costRecipe;
    private RecipeCategory recipeCategory;
    private String imageUrl;
    private Set<SupplyDto> supplies;
    private Set<SubRecipeOutputDto> recipes;
}
