package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "supply_loss")
@Getter
@Setter
public class SupplyLoss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supply_id", nullable = false)
    private SupplyEntity supply;

    @Column(name = "lost_quantity", nullable = false)
    private Double lostQuantity;

    private String description;

    @Column(name = "loss_date", nullable = false)
    private LocalDate lossDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "image_url", nullable = true)
    private String imageUrl ;


}
