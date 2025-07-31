package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.RecipeProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeProductionRepository extends JpaRepository<RecipeProduction, Long> {
    List<RecipeProduction> findAllByUserId(Long userId);
}
