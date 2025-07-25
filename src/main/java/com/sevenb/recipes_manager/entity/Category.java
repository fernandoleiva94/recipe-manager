package com.sevenb.recipes_manager.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String description;
    private Long userId;
}
