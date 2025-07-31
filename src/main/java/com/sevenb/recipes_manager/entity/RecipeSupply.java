package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipe_supply")
@Getter
@Setter
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

    public Double cost(){
        double cost = this.getQuantity() / supply.getQuantity() * getSupply().getPrice();
        return Math.round(cost * 100.0) / 100.0;
    }
}
