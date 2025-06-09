package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Long> {
    List<DishEntity> findAllByUserId(Long userId);
}
