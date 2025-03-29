package com.sevenb.recipes_manager.dto;

import lombok.Data;

@Data
public class SupplyDto {

    private Long id;
    private String name;
    private Double price;
    private Double quantity;

}
