package com.sevenb.recipes_manager.dto;

import lombok.Data;
import java.util.List;

@Data
public class DishDto {
    private String name;
    private String description;
    private List<SupplyDto> supplies;
    private List<RecipeDto> recipes;
    private Double profitMargin;
    private Long userId;
    private String imageUrl;
}
