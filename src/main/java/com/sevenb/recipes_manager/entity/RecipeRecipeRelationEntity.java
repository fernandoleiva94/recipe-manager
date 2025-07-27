package com.sevenb.recipes_manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipe_recipe_relation")
@Getter
@Setter
public class RecipeRecipeRelationEntity {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "recipe_id", nullable = false, foreignKey = @ForeignKey(name = "fk_recipe"))
        @JsonBackReference
        private Recipe recipe;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sub_recipe_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sub_recipe"))
        private Recipe subRecipe;
        private Double quantity;


        public Double cost() {
                double cost =  this.quantity / subRecipe.getQuantity() * subRecipe.cost();
         return Math.round(cost * 100.0) / 100.0;
        }

    }


