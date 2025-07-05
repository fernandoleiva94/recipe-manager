package com.sevenb.recipes_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recipe_category", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "description"})
})
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "user_id", nullable = false)
    @JsonIgnore
    private Long userId;

    // Getters y Setters
}
