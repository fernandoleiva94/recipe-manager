package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // category ahora es LAZY: la traemos con EntityGraph para evitar
    // que Jackson intente serializar un proxy no inicializado (ByteBuddyInterceptor error).
    @EntityGraph(attributePaths = "category")
    List<Recipe> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {
        "category",
        "recipeSupplies.supply",
        "recipeRecipeRelations.subRecipe"
    })
    Optional<Recipe> findWithDetailsById(Long id);
}


