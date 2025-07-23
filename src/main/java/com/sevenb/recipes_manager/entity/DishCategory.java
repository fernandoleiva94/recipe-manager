package com.sevenb.recipes_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "dish_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "description"})
})
public class DishCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "user_id", nullable = false)
    @JsonIgnore
    private Long userId;


}
