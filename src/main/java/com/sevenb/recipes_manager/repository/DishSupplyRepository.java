package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.entity.DishSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishSupplyRepository extends JpaRepository<DishSupply, Long> {
    List<DishSupply> findByDish(DishEntity dish);
}

