package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

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
        private Long userId;
        private String imageUrl;

        // No cascada REMOVE hacia categoría: una categoría puede ser compartida por varios platos.
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private DishCategory category;

        // Set en vez de List: evita MultipleBagFetchException al hacer JOIN FETCH
        // de dos colecciones (supplies y recipes) en la misma consulta.
        @OneToMany(mappedBy = "dish",fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<DishSupply> supplies = new HashSet<>();

        @OneToMany(mappedBy = "dish",fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<DishRecipe> recipes = new HashSet<>();




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
