package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.dto.DishCategoryDto;
import com.sevenb.recipes_manager.dto.DishDto;
import com.sevenb.recipes_manager.dto.DishOutpuDto;
import com.sevenb.recipes_manager.dto.recipe.RecipeInputDto;
import com.sevenb.recipes_manager.dto.SupplyDto;
import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.entity.DishRecipe;
import com.sevenb.recipes_manager.entity.DishSupply;
import com.sevenb.recipes_manager.repository.DishCategoryRepository;
import com.sevenb.recipes_manager.repository.DishRepository;
import com.sevenb.recipes_manager.repository.RecipeRepository;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final SupplyRepository supplyRepository;
    private final RecipeRepository recipeRepository;
    private final DishCategoryRepository categoryRepository;


    public DishService(DishRepository dishRepository , SupplyRepository supplyRepository,
                       RecipeRepository recipeRepository, DishCategoryRepository categoryRepository) {
        this.dishRepository = dishRepository;
        this.supplyRepository = supplyRepository;
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
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
        dish.setCategory(categoryRepository.findById(dishDTO.getCategoryId()).orElseThrow());

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
            dish.setCategory(categoryRepository.findById(dishDTO.getCategoryId()).orElseThrow());
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


        if(dishEntity.getCategory() != null) {
            DishCategoryDto dishCategoryDto = new DishCategoryDto();
            dishCategoryDto.setId(dishEntity.getCategory().getId());
            dishCategoryDto.setDescription(dishEntity.getCategory().getDescription());
            outpuDto.setDishCategory(dishCategoryDto);
        }

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

        Set<RecipeInputDto> recipes = dishEntity.getRecipes().stream()
                .map(recipe ->{
                    RecipeInputDto recipeInputDto = new RecipeInputDto();
                    recipeInputDto.setId(recipe.getRecipe().getId());
                    recipeInputDto.setDescription(recipe.getRecipe().getDescription());
                    recipeInputDto.setName(recipe.getRecipe().getName());
                    recipeInputDto.setCostTotal(recipe.cost());
                    recipeInputDto.setUnit(recipe.getRecipe().getUnit());
                    recipeInputDto.setQuantity(recipe.getQuantity());
                    return recipeInputDto;
                }).collect(Collectors.toSet());

        outpuDto.setRecipe(recipes);
        outpuDto.setCostDish(dishEntity.cost());
        outpuDto.setPrice(dishEntity.price());
        outpuDto.setProfitMargin(dishEntity.getProfitMargin());


        return outpuDto;
    }

    private DishOutpuDto toBasicDishDto(DishEntity dishEntity){
        DishOutpuDto outpuDto = new DishOutpuDto();
        outpuDto.setId(dishEntity.getId());
        outpuDto.setDescription(dishEntity.getDescription());
        outpuDto.setName(dishEntity.getName());
        outpuDto.setImageUrl(dishEntity.getImageUrl());

        outpuDto.setProfitMargin(dishEntity.getProfitMargin());

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

        Set<RecipeInputDto> recipes = dishEntity.getRecipes().stream()
                .map(recipe ->{
                    RecipeInputDto recipeInputDto = new RecipeInputDto();
                    recipeInputDto.setId(recipe.getRecipe().getId());
                    recipeInputDto.setDescription(recipe.getRecipe().getDescription());
                    recipeInputDto.setName(recipe.getRecipe().getName());
                    recipeInputDto.setCostTotal(recipe.cost());
                    recipeInputDto.setUnit(recipe.getRecipe().getUnit());
                    recipeInputDto.setQuantity(recipe.getQuantity());
                    return recipeInputDto;
                }).collect(Collectors.toSet());

        outpuDto.setRecipe(recipes);

        return outpuDto;
    }


}
