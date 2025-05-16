package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
@Data
public class DishEntity {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String description;
        private Double profitMargin;

        @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<DishSupply> supplies;

        @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<DishRecipe> recipes;




    @Override
    public String toString() {
        return "DishEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", supplies=" + supplies +
                ", recipes=" + recipes +
                '}';
    }


    public Double cost(){
            double totalCost =
                    this.recipes.stream().mapToDouble(DishRecipe::cost).sum() +
                            this.supplies.stream().mapToDouble(DishSupply::cost).sum();

            return BigDecimal.valueOf(totalCost)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
    }

    public  Double price(){
        double price =  cost() *  (1 + (profitMargin / 100));
        return  Math.round(price * 100.0) / 100.0;
    }

}
