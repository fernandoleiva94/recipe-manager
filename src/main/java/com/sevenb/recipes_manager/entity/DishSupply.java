package com.sevenb.recipes_manager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dish_supplies")
public class DishSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private DishEntity dish;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private SupplyEntity supply;

    private Double quantity; // Cantidad usada en el plato

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DishEntity getDish() {
        return dish;
    }

    public void setDish(DishEntity dish) {
        this.dish = dish;
    }

    public SupplyEntity getSupply() {
        return supply;
    }

    public void setSupply(SupplyEntity supply) {
        this.supply = supply;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }


    public Double cost(){
        return this.quantity * (this.supply.getPrice() / this.supply.getQuantity());
    }
}