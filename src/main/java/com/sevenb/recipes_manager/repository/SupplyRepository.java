package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.SupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<SupplyEntity, Long> {

    // Method to search for supplies containing part of the name
    List<SupplyEntity> findByNameContainingIgnoreCase(String name);
    List<SupplyEntity> findAllByUserId(Long userId);
    List<SupplyEntity> findAllByUserIdAndCheckStock(Long userId, boolean checkStock);


    List<SupplyEntity> findByStockLessThanEqual(Double stock);
    @Query("SELECT s FROM SupplyEntity s WHERE s.stock <= s.minStock and s.checkStock = true")
    List<SupplyEntity> findLowStockSupplies();
}