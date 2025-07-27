package com.sevenb.recipes_manager.dto;

import com.sevenb.recipes_manager.dto.recipe.RecipeInputDto;
import lombok.Data;
import java.util.List;

@Data
public class DishDto {
    private String name;
    private String description;
    private List<SupplyDto> supplies;
    private List<RecipeInputDto> recipes;
    private Double profitMargin;
    private Long userId;
    private String imageUrl;
    private Boolean deleteImage;
    private Long categoryId;
}
