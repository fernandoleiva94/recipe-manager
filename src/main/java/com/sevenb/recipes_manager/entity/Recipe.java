package com.sevenb.recipes_manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
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
    private String imageUrl;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeSupply> recipeSupplies = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeCategory category ;

    @OneToMany(mappedBy = "recipe",fetch =  FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("recipe-main")
    private Set<RecipeRecipeRelationEntity> recipeRecipeRelations = new HashSet<>();



    public Double cost(){
        double supplyCost = recipeSupplies.stream()
                .mapToDouble(RecipeSupply::cost)
                .sum();

        double subRecipeCost = recipeRecipeRelations.stream()
                .mapToDouble(RecipeRecipeRelationEntity::cost)
                .sum();

        return Math.round((supplyCost + subRecipeCost) * 100.0) / 100.0;
    }

}
