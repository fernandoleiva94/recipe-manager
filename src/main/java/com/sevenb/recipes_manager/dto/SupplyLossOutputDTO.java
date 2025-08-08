package com.sevenb.recipes_manager.dto;

import lombok.Data;


import java.time.LocalDate;


@Data
public class SupplyLossOutputDTO {
    private Long id;
    private Double quantity;
    private String description;
    private LocalDate lossDate;
    private String SupplyName;
    private Long supplyId;
    private String unit;
    private String imageUrl;

}


