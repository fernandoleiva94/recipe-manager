package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //no tiene que ser null.
    private String name;
    private Long portion;
    private Double weightFinal;
    private String description;


    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeSupply> recipeSupplies = new HashSet<>();;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RecipeSupply> getRecipeSupplies() {
        return recipeSupplies;
    }

    public void setRecipeSupplies(Set<RecipeSupply> recipeSupplies) {
        this.recipeSupplies = recipeSupplies;
    }

    public Long getPortion() {
        return portion;
    }

    public void setPortion(Long portion) {
        this.portion = portion;
    }

    public Double getWeightFinal() {
        return weightFinal;
    }

    public void setWeightFinal(Double weightFinal) {
        this.weightFinal = weightFinal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Double cost(){
        return this.recipeSupplies.stream()
                .mapToDouble(RecipeSupply::cost)
                .sum();
    }

}
