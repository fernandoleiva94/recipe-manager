package com.sevenb.recipes_manager.service;


import com.sevenb.recipes_manager.Exception.CannotDeleteSupplyException;
import com.sevenb.recipes_manager.dto.recipe.RecipeInputDto;
import com.sevenb.recipes_manager.dto.recipe.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.SupplyDto;
import com.sevenb.recipes_manager.dto.recipe.SubRecipeOutputDto;
import com.sevenb.recipes_manager.entity.*;
import com.sevenb.recipes_manager.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private RecipeSupplyRepository recipeIngredientRepository;

    @Autowired
    private RecipeCategoryRepository categoryRepository;

    @Transactional
    public RecipeOuputDto createRecipe(RecipeInputDto recipeInputDto) {

        Recipe recipe = new Recipe();
        recipe.setDescription(recipeInputDto.getDescription());
        recipe.setName(recipeInputDto.getName().toUpperCase(Locale.ROOT));
        recipe.setQuantity(recipeInputDto.getQuantity());
        recipe.setUnit(recipeInputDto.getUnit());
        recipe.setCategory(categoryRepository.findById(recipeInputDto.getCategoryId()).orElseThrow());
        recipe.setUserId(recipeInputDto.getUserId());
        recipe.setImageUrl(recipeInputDto.getImageUrl());

        recipeInputDto.getIngredients().forEach(recipeSupplyDto -> {
            SupplyEntity supplyEntity = supplyRepository
                    .findById(recipeSupplyDto.getSupplyId())
                    .orElseThrow(() -> new RuntimeException("Supply id: " + recipeSupplyDto.getSupplyId() + " not found"));
            RecipeSupply recipeSupply = new RecipeSupply();
            recipeSupply.setRecipe(recipe);
            recipeSupply.setSupply(supplyEntity);
            recipeSupply.setQuantity(recipeSupplyDto.getQuantity());
            recipe.getRecipeSupplies().add(recipeSupply);
        });

        recipeInputDto.getRecipes().forEach(recipeRecipeDto -> {
            Recipe subRecipe = recipeRepository
                    .findById(recipeRecipeDto.getRecipeId())
                    .orElseThrow(() -> new RuntimeException("Supply not found"));
            RecipeRecipeRelationEntity recipeRecipeRelation = new RecipeRecipeRelationEntity();

            recipeRecipeRelation.setRecipe(recipe);
            recipeRecipeRelation.setSubRecipe(subRecipe);
            recipeRecipeRelation.setQuantity(recipeRecipeDto.getQuantity());
            recipe.getRecipeRecipeRelations().add(recipeRecipeRelation);
        });

        return toRecipeDTO(recipeRepository.save(recipe));
    }

    public Set<RecipeOuputDto> getAllRecipes(Long userId) {
        Set<RecipeOuputDto> recipeOuputDtos = new HashSet<>();
        recipeRepository.findAllByUserId(userId).forEach(l
                -> recipeOuputDtos.add(toRecipeDTOBasic(l)));
        return recipeOuputDtos;
    }

    public RecipeOuputDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        return toRecipeDTO(recipe);
    }

    @Transactional
    public RecipeOuputDto updateRecipe(RecipeInputDto recipeInputDto, Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setName(recipeInputDto.getName().toUpperCase(Locale.ROOT));
        recipe.setDescription(recipeInputDto.getDescription());
        recipe.setQuantity(recipeInputDto.getQuantity());
        recipe.setUnit(recipeInputDto.getUnit());
        // Mejor manejo de categoría: siempre buscar y setear la entidad
        RecipeCategory category = categoryRepository.findById(recipeInputDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        recipe.setCategory(category);
        recipe.setImageUrl(recipeInputDto.getImageUrl());

        // Limpiar y actualizar ingredientes
        recipe.getRecipeSupplies().clear();
        recipeInputDto.getIngredients().forEach(recipeSupplyDto -> {
            SupplyEntity supplyEntity = supplyRepository
                    .findById(recipeSupplyDto.getSupplyId())
                    .orElseThrow(() -> new RuntimeException("Supply not found"));
            RecipeSupply recipeSupply = new RecipeSupply();
            recipeSupply.setRecipe(recipe);
            recipeSupply.setSupply(supplyEntity);
            recipeSupply.setQuantity(recipeSupplyDto.getQuantity());
            recipe.getRecipeSupplies().add(recipeSupply);
        });

        // Limpiar y actualizar sub-recetas
        recipe.getRecipeRecipeRelations().clear();
        recipeInputDto.getRecipes().forEach(recipeRecipeDto -> {
            Recipe subRecipe = recipeRepository
                    .findById(recipeRecipeDto.getRecipeId())
                    .orElseThrow(() -> new RuntimeException("Sub-recipe not found"));
            RecipeRecipeRelationEntity relation = new RecipeRecipeRelationEntity();
            relation.setRecipe(recipe);
            relation.setSubRecipe(subRecipe);
            relation.setQuantity(recipeRecipeDto.getQuantity());
            recipe.getRecipeRecipeRelations().add(relation);
        });

        recipeRepository.save(recipe);
        return toRecipeDTO(recipe);
    }


    public void deleteRecipe(Long id) {
        try {
            recipeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new CannotDeleteSupplyException("El insumo está siendo ocupado por algún producto final. Por favor, elimínalos primero.");
        }

    }

    public RecipeOuputDto toRecipeDTO(Recipe recipe) {
        RecipeOuputDto dto = new RecipeOuputDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setQuantity(recipe.getQuantity());
        dto.setUnit(recipe.getUnit());
        dto.setCostRecipe(recipe.cost());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setDescription(recipe.getDescription());
        dto.setRecipeCategory(recipe.getCategory());

        // Transformar recipeSupplies
        Set<SupplyDto> supplies = recipe.getRecipeSupplies().stream()
                .map(supply -> {
                    SupplyDto supplyDTO = new SupplyDto();
                    supplyDTO.setName(supply.getSupply().getName());
                    supplyDTO.setId(supply.getSupply().getId());
                    supplyDTO.setQuantity(supply.getQuantity());
                    supplyDTO.setPrice(supply.cost());
                    supplyDTO.setUnit(supply.getSupply().getUnit());
                    return supplyDTO;
                })
                .collect(Collectors.toSet());
        dto.setSupplies(supplies);


        Set<SubRecipeOutputDto> subRecipeOutputDtos = recipe.getRecipeRecipeRelations().stream()
                .map(subRecipe -> {
                    SubRecipeOutputDto recipeOutputDto = new SubRecipeOutputDto();
                    recipeOutputDto.setName(subRecipe.getSubRecipe().getName());
                    recipeOutputDto.setId(subRecipe.getSubRecipe().getId());
                    recipeOutputDto.setQuantity(subRecipe.getQuantity());
                    recipeOutputDto.setCost(subRecipe.cost());
                    recipeOutputDto.setUnit(subRecipe.getSubRecipe().getUnit());
                    return recipeOutputDto;
                })
                .collect(Collectors.toSet());
        dto.setRecipes(subRecipeOutputDtos);

        return dto;
    }


    public RecipeOuputDto toRecipeDTOBasic(Recipe recipe) {
        RecipeOuputDto dto = new RecipeOuputDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setQuantity(recipe.getQuantity());
        dto.setUnit(recipe.getUnit());
        //dto.setCostRecipe(recipe.cost());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setDescription(recipe.getDescription());
        dto.setRecipeCategory(recipe.getCategory());

        return dto;
    }


}
