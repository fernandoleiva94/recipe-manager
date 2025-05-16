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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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


    public List<DishOutpuDto> getAllDishes() {
        List<DishOutpuDto> dishOutpuDtos = new ArrayList<>();
        dishRepository.findAll().forEach(l
                ->dishOutpuDtos.add(toDishDto(l)));
        return dishOutpuDtos;
    }

    public Optional<DishEntity> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    @Transactional
    public DishEntity createDish(DishDto dishDTO) {
        DishEntity dish = new DishEntity();
        dish.setName(dishDTO.getName());
        dish.setDescription(dishDTO.getDescription());
        dish.setProfitMargin(dishDTO.getProfitMargin());

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

    public DishEntity updateDish(Long id, DishEntity dishDetails) {
        return dishRepository.findById(id).map(dish -> {
            dish.setName(dishDetails.getName());
            dish.setDescription(dishDetails.getDescription());
            return dishRepository.save(dish);
        }).orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }


    private DishOutpuDto toDishDto(DishEntity dishEntity){
        DishOutpuDto outpuDto = new DishOutpuDto();
        outpuDto.setId(dishEntity.getId());
        outpuDto.setDescription(dishEntity.getDescription());
        outpuDto.setName(dishEntity.getName());

        Set<SupplyDto> supplies = dishEntity.getSupplies().stream()
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

        outpuDto.setSupplies(supplies);

        Set<RecipeDto> recipes = dishEntity.getRecipes().stream()
                .map(recipe ->{
                    RecipeDto recipeDto = new RecipeDto();
                    recipeDto.setId(recipe.getId());
                    recipeDto.setDescription(recipe.getRecipe().getDescription());
                    recipeDto.setName(recipe.getRecipe().getName());
                    recipeDto.setCostTotal(recipe.cost());
                    return recipeDto;
                }).collect(Collectors.toSet());

        outpuDto.setRecipe(recipes);
        outpuDto.setCostDish(dishEntity.cost());
        outpuDto.setPrice(dishEntity.price());
        outpuDto.setProfitMargin(dishEntity.getProfitMargin());


        return outpuDto;
    }
}
