package com.sevenb.recipes_manager.dto;

import com.sevenb.recipes_manager.entity.RecipeSupply;
import lombok.Data;

import java.util.HashSet;
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
    private Set<SupplyDto> supplies;
}
