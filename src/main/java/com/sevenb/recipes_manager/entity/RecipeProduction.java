package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_production")
@Getter
@Setter
public class RecipeProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Double quantityProduced;
    private String unit;
    private LocalDateTime productionDate;
    private Long userId;
    private Double expectedQuantity; // cantidad teórica según receta
    private Double yield; // rendimiento real (quantityProduced / expectedQuantity)
    private Double cost; // costo real de la producción
    private String notes;
    private LocalDateTime expirationDate; // observaciones, mermas, etc.
}

