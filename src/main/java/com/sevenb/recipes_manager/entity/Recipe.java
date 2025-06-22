package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //no tiene que ser null.
    private String name;
    private Double quantity;
    private String  unit;
    private String description;
    private Long userId;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeSupply> recipeSupplies = new HashSet<>();


    public Double cost(){
        double sum = this.recipeSupplies.stream()
                .mapToDouble(RecipeSupply::cost)
                .sum();
        return Math.round(sum * 100.0) / 100.0;
    }

}
