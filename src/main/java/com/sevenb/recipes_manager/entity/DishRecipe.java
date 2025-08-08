package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "dish_recipes")
public class DishRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private DishEntity dish;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    private Double quantity; // Cantidad de veces que se usa la receta en el plato

    public Double cost(){
        double cost = this.getQuantity() * (this.recipe.cost() / this.recipe.getQuantity());
       return Math.round(cost * 100.0) / 100.0;
    }
}
