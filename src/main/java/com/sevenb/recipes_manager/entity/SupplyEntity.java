package com.sevenb.recipes_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "supplies")
@Getter
@Setter
public class SupplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double quantity;
    private Double price;
    private String unit;
    private String description;
    private Double wastage; //merma
    private Long userId;

    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<RecipeSupply> recipeSupplies; // Relación con la tabla intermedia

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}

