package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.RecipeSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeSupplyRepository extends JpaRepository<RecipeSupply, Long> {

}
