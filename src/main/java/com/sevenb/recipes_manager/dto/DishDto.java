package com.sevenb.recipes_manager.dto;

import lombok.Data;
import java.util.List;

@Data
public class DishDto {
    private String name;
    private String description;
    private Double weight;
    private List<SupplyDto> supplies;
    private List<RecipeDto> recipes;
    private Double quantity;
    private Double profitMargin;



}
