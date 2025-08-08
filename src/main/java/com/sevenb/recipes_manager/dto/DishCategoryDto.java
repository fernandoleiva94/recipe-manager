package com.sevenb.recipes_manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIncludeProperties(value = "DishCategory")
public class DishCategoryDto
{

    private Long id;
    private String description;

}
