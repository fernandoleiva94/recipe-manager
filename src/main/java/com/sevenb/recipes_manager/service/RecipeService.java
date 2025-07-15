package com.sevenb.recipes_manager.service;


import com.sevenb.recipes_manager.Exception.CannotDeleteSupplyException;
import com.sevenb.recipes_manager.dto.RecipeDto;
import com.sevenb.recipes_manager.dto.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.RecipeSupplyDto;
import com.sevenb.recipes_manager.dto.SupplyDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.entity.RecipeCategory;
import com.sevenb.recipes_manager.entity.RecipeSupply;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
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
    public Recipe createRecipe(RecipeDto recipeDto){

        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDto.getDescription());
        recipe.setName(recipeDto.getName());
        recipe.setQuantity(recipeDto.getQuantity());
        recipe.setUnit(recipeDto.getUnit());
        recipe.setCategory(categoryRepository.findById(recipeDto.getCategoryId()).orElseThrow());
        recipe.getCategory().setId(recipeDto.getCategoryId());
        recipe.setUserId(recipeDto.getUserId());


        recipeDto.getIngredients().forEach( recipeSupplyDto -> {
            SupplyEntity supplyEntity = supplyRepository
                    .findById(recipeSupplyDto.getSupplyId())
                    .orElseThrow(()-> new RuntimeException("Supply not found"));


            recipe.setName(recipe.getName().toUpperCase(Locale.ROOT));
            RecipeSupply recipeSupply = new RecipeSupply();
            recipeSupply.setRecipe(recipe);
            recipeSupply.setSupply(supplyEntity);
            recipeSupply.setQuantity(recipeSupplyDto.getQuantity());

            recipe.getRecipeSupplies().add(recipeSupply);

        });

        return recipeRepository.save(recipe);
    }

    public Set<RecipeOuputDto> getAllRecipes(Long userId) {
        Set<RecipeOuputDto> recipeOuputDtos = new HashSet<>();
        recipeRepository.findAllByUserId(userId).forEach(l
                -> recipeOuputDtos.add(toRecipeDTO(l)));
        return recipeOuputDtos;
    }

    public RecipeOuputDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(()-> new RuntimeException("Recipe not found"));
        return toRecipeDTO(recipe);
    }

    @Transactional
    public RecipeOuputDto updateRecipe(RecipeDto recipeDto, Long id){
        DecimalFormat df = new DecimalFormat("#.00");
        Recipe recipe = recipeRepository.findById(id).orElseThrow(()-> new RuntimeException("Recipe not found"));
            Set<RecipeSupply> recipeSupplies = new HashSet<>();
            recipe.setId(id);
            recipe.setName(recipeDto.getName().toUpperCase(Locale.ROOT));
            recipe.setDescription(recipeDto.getDescription());
            recipe.setQuantity(recipeDto.getQuantity());
            recipe.setUnit(recipeDto.getUnit());
            recipe.getCategory().setId(recipeDto.getCategoryId());
            recipe.setImageUrl(recipeDto.getImageUrl());
            recipe.getRecipeSupplies().clear();

            recipeDto.getIngredients().forEach( recipeSupplyDto -> {
                SupplyEntity supplyEntity = supplyRepository
                        .findById(recipeSupplyDto.getSupplyId())
                        .orElseThrow(()-> new RuntimeException("Supply not found"));


                RecipeSupply recipeSupply = new RecipeSupply();
                recipeSupply.setRecipe(recipe);
                recipeSupply.setSupply(supplyEntity);
                recipeSupply.setQuantity(recipeSupplyDto.getQuantity());

                recipe.getRecipeSupplies().add(recipeSupply);

            });

            recipeRepository.save(recipe);
        return toRecipeDTO(recipe);

    }




    public void deleteRecipe(Long id) {
        try{
            recipeRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
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
                    supplyDTO.setId(supply.getId());
                    supplyDTO.setName(supply.getSupply().getName());
                    supplyDTO.setId(supply.getSupply().getId());
                    supplyDTO.setQuantity(supply.getQuantity());
                    supplyDTO.setPrice(supply.cost());
                    supplyDTO.setUnit(supply.getSupply().getUnit());
                    return supplyDTO;
                })
                .collect(Collectors.toSet());
        dto.setSupplies(supplies);

        return dto;
    }

}
