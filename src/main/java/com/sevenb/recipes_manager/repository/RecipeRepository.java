package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByUserId(Long userId);
}
