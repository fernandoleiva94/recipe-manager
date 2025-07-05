package com.sevenb.recipes_manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class SupplyLossOutputDTO {
    private Long id;
    private BigDecimal quantity;
    private String description;
    private LocalDate lossDate;
    private String SupplyName;
    private String unit;
    private String imageUrl;

}


