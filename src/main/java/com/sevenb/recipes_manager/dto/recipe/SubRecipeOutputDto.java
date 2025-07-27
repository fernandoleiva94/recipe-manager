package com.sevenb.recipes_manager.dto.recipe;

import lombok.Data;

@Data
public class SubRecipeOutputDto {


    private Long id;
    private String name;
    private String unit;
    private Double quantity;
    private Double cost;

}
