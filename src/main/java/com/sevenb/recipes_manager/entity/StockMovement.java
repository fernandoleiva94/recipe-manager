package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SupplyEntity supply;
    private LocalDateTime date;
    private String type;         // "ENTRADA" o "SALIDA"
    private Double quantity;
    private String reason;       // ejemplo: "Compra", "Producción", "Ajuste manual"
}