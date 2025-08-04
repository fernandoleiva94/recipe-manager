package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_loss")
@Getter
@Setter
public class RecipeLoss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Double quantityLost;
    private String unit;
    private LocalDateTime lossDate;
    private Long userId;
    private String notes;
    private String imageUrl;
}

