package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.dto.DishDto;
import com.sevenb.recipes_manager.dto.DishOutpuDto;
import com.sevenb.recipes_manager.dto.RecipeDto;
import com.sevenb.recipes_manager.dto.SupplyDto;
import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.entity.DishRecipe;
import com.sevenb.recipes_manager.entity.DishSupply;
import com.sevenb.recipes_manager.repository.DishRepository;
import com.sevenb.recipes_manager.repository.RecipeRepository;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final SupplyRepository supplyRepository;
    private final RecipeRepository recipeRepository;

    public DishService(DishRepository dishRepository , SupplyRepository supplyRepository,
                       RecipeRepository recipeRepository) {
        this.dishRepository = dishRepository;
        this.supplyRepository = supplyRepository;
        this.recipeRepository = recipeRepository;
    }


    public List<DishOutpuDto> getAllDishes(Long userId) {
        List<DishOutpuDto> dishOutpuDtos = new ArrayList<>();
        dishRepository.findAllByUserId(userId).forEach(l
                ->dishOutpuDtos.add(toDishDto(l)));
        return dishOutpuDtos;
    }

    public DishOutpuDto getDishById(Long id) {
        DishEntity dishOutput = dishRepository.findById(id).orElse(null);
        return Objects.isNull(dishOutput) ?  null : toDishDto(dishOutput);
    }

    @Transactional
    public DishEntity createDish(DishDto dishDTO) {
        DishEntity dish = new DishEntity();
        dish.setName(dishDTO.getName().toUpperCase(Locale.ROOT));
        dish.setDescription(dishDTO.getDescription());
        dish.setProfitMargin(dishDTO.getProfitMargin());
        dish.setUserId(dishDTO.getUserId());
        dish.setImageUrl(dishDTO.getImageUrl());

        // 🔹 Guardamos Dish base
        DishEntity savedDish = dishRepository.save(dish);

        // 🔹 Mapeamos Supplies
        List<DishSupply> dishSupplies = dishDTO.getSupplies()
                .stream()
                .map(dtoSupply -> {
                    DishSupply dishSupply = new DishSupply();
                    dishSupply.setDish(savedDish);
                    dishSupply.setQuantity(dtoSupply.getQuantity());
                    dishSupply.setSupply(supplyRepository.findById(dtoSupply.getId()).orElseThrow());
                    return dishSupply;
                })
                .collect(Collectors.toList());

        // 🔹 Mapeamos Recipes
        List<DishRecipe> dishRecipes = dishDTO.getRecipes()
                .stream()
                .map(dtoRecipe -> {
                    DishRecipe dishRecipe = new DishRecipe();
                    dishRecipe.setDish(savedDish);
                    dishRecipe.setRecipe(recipeRepository.findById(dtoRecipe.getId()).orElseThrow());
                    dishRecipe.setQuantity(dtoRecipe.getQuantity());
                    return dishRecipe;
                })
                .collect(Collectors.toList());

        savedDish.setSupplies(dishSupplies);
        savedDish.setRecipes(dishRecipes);

        return dishRepository.save(savedDish);
    }




    @Transactional
    public DishOutpuDto updateDish(Long id, DishDto dishDTO) {

        DishEntity dishEntity = dishRepository.findById(id).map(dish -> {
            dish.setName(dishDTO.getName());
            dish.setDescription(dishDTO.getDescription());
            dish.setProfitMargin(dishDTO.getProfitMargin());
            dish.setImageUrl(dishDTO.getImageUrl());
            return dish;
        }).orElseThrow(() -> new RuntimeException("Dish not found"));

        dishEntity.getSupplies().clear();
        dishEntity.getRecipes().clear();

        // 🔹 Mapeamos Supplies
        List<DishSupply> dishSupplies = dishDTO.getSupplies()
                .stream()
                .map(dtoSupply -> {
                    DishSupply dishSupply = new DishSupply();
                    dishSupply.setDish(dishEntity);
                    dishSupply.setQuantity(dtoSupply.getQuantity());
                    dishSupply.setSupply(supplyRepository.findById(dtoSupply.getId()).orElseThrow());
                    return dishSupply;
                })
                .collect(Collectors.toList());

        dishEntity.getSupplies().addAll(dishSupplies);

        // 🔹 Mapeamos Recipes
        List<DishRecipe> dishRecipes = dishDTO.getRecipes()
                .stream()
                .map(dtoRecipe -> {
                    DishRecipe dishRecipe = new DishRecipe();
                    dishRecipe.setDish(dishEntity);
                    dishRecipe.setRecipe(recipeRepository.findById(dtoRecipe.getId()).orElseThrow());
                    dishRecipe.setQuantity(dtoRecipe.getQuantity());
                    return dishRecipe;
                })
                .collect(Collectors.toList());


        dishEntity.getRecipes().addAll(dishRecipes);

        return toDishDto(dishRepository.save(dishEntity));
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }


    private DishOutpuDto toDishDto(DishEntity dishEntity){
        DishOutpuDto outpuDto = new DishOutpuDto();
        outpuDto.setId(dishEntity.getId());
        outpuDto.setDescription(dishEntity.getDescription());
        outpuDto.setName(dishEntity.getName());
        outpuDto.setImageUrl(dishEntity.getImageUrl());

        Set<SupplyDto> supplies = dishEntity.getSupplies().stream()
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

        outpuDto.setSupplies(supplies);

        Set<RecipeDto> recipes = dishEntity.getRecipes().stream()
                .map(recipe ->{
                    RecipeDto recipeDto = new RecipeDto();
                    recipeDto.setId(recipe.getRecipe().getId());
                    recipeDto.setDescription(recipe.getRecipe().getDescription());
                    recipeDto.setName(recipe.getRecipe().getName());
                    recipeDto.setCostTotal(recipe.cost());
                    recipeDto.setUnit(recipe.getRecipe().getUnit());
                    recipeDto.setQuantity(recipe.getQuantity());
                    return recipeDto;
                }).collect(Collectors.toSet());

        outpuDto.setRecipe(recipes);
        outpuDto.setCostDish(dishEntity.cost());
        outpuDto.setPrice(dishEntity.price());
        outpuDto.setProfitMargin(dishEntity.getProfitMargin());


        return outpuDto;
    }
}
