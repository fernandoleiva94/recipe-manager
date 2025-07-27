package com.sevenb.recipes_manager.dto.recipe;


import lombok.Data;

@Data
public class SubRecipeInputDto {
    private Long recipeId; // ID del insumo
    private Double quantity; // Cantidad de este insumo en la receta
}
