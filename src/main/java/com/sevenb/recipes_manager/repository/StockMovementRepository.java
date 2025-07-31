package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findBySupplyId(Long supplyId);
}