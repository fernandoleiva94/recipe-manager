package com.sevenb.recipes_manager.dto;

import lombok.Data;

@Data
public class RecipeSupplyDto {

        private Long supplyId; // ID del insumo
        private Double quantity; // Cantidad de este insumo en la receta
}



