package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe_supply")
public class RecipeSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false, foreignKey = @ForeignKey(name = "fk_recipe"))
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id", nullable = false, foreignKey = @ForeignKey(name = "fk_supply"))
    private SupplyEntity supply;

    private Double quantity;

    // Getters y setters



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public SupplyEntity getSupply() {
        return supply;
    }

    public void setSupply(SupplyEntity supply) {
        this.supply = supply;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }


    public Double cost(){
        double cost = this.getQuantity() / supply.getQuantity() * getSupply().getPrice();
        return Math.round(cost * 100.0) / 100.0;
    }
}
