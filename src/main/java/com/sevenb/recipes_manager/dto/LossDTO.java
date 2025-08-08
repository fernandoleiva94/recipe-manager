package com.sevenb.recipes_manager.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LossDTO {
    private Long supplyId;
    private Double lostQuantity;
    private String description;
    private LocalDate lossDate;
    private String imageUrl;
}
