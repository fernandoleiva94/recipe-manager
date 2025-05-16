package com.sevenb.recipes_manager.service;


import com.sevenb.recipes_manager.dto.RecipeDto;
import com.sevenb.recipes_manager.dto.RecipeOuputDto;
import com.sevenb.recipes_manager.dto.RecipeSupplyDto;
import com.sevenb.recipes_manager.dto.SupplyDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.entity.RecipeSupply;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.RecipeSupplyRepository;
import com.sevenb.recipes_manager.repository.RecipeRepository;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final ModelMapper modelMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private RecipeSupplyRepository recipeIngredientRepository;

    public RecipeService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Recipe createRecipe(Recipe recipe , Set<RecipeSupplyDto> recipeSupplyDtos){

        recipeSupplyDtos.forEach( recipeSupplyDto -> {
            SupplyEntity supplyEntity = supplyRepository
                    .findById(recipeSupplyDto.getSupplyId())
                    .orElseThrow(()-> new RuntimeException("Supply not found"));


            RecipeSupply recipeSupply = new RecipeSupply();
            recipeSupply.setRecipe(recipe);
            recipeSupply.setSupply(supplyEntity);
            recipeSupply.setQuantity(recipeSupplyDto.getQuantity());

            recipe.getRecipeSupplies().add(recipeSupply);

        });
        return recipeRepository.save(recipe);
    }

    public Set<RecipeOuputDto> getAllRecipes() {
        Set<RecipeOuputDto> recipeOuputDtos = new HashSet<>();
        recipeRepository.findAll().forEach(l
                -> recipeOuputDtos.add(toRecipeDTO(l)));
        return recipeOuputDtos;
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe updateRecipe(RecipeDto recipeDto, Long id){
        DecimalFormat df = new DecimalFormat("#.00");
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(recipeDto.getName());
        recipe.setQuantity(recipeDto.getQuantity());
        recipe.setUnit(recipeDto.getUnit());

        return null;

    }




    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }


    public RecipeOuputDto toRecipeDTO(Recipe recipe) {
        RecipeOuputDto dto = new RecipeOuputDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setQuantity(recipe.getQuantity());
        dto.setUnit(recipe.getUnit());
        dto.setCostRecipe(recipe.cost());

        // Transformar recipeSupplies
        Set<SupplyDto> supplies = recipe.getRecipeSupplies().stream()
                .map(supply -> {
                    SupplyDto supplyDTO = new SupplyDto();
                    supplyDTO.setId(supply.getId());
                    supplyDTO.setName(supply.getSupply().getName());
                    supplyDTO.setId(supply.getSupply().getId());
                    supplyDTO.setQuantity(supply.getQuantity());
                    supplyDTO.setPrice(supply.cost());
                    return supplyDTO;
                })
                .collect(Collectors.toSet());
        dto.setSupplies(supplies);

        return dto;
    }
}
