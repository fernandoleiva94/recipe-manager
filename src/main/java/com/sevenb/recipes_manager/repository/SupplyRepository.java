package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.SupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<SupplyEntity, Long> {

    // Method to search for supplies containing part of the name
    List<SupplyEntity> findByNameContainingIgnoreCase(String name);
    List<SupplyEntity> findAllByUserId(Long userId);
}