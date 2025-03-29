package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.entity.DishSupply;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.DishRepository;
import com.sevenb.recipes_manager.repository.DishSupplyRepository;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import org.springframework.stereotype.Service;

@Service
public class DishSupplyService {

    private final DishSupplyRepository dishSupplyRepository;
    private final DishRepository dishRepository;
    private final SupplyRepository supplyRepository;

    public DishSupplyService(DishSupplyRepository dishSupplyRepository, DishRepository dishRepository, SupplyRepository supplyRepository) {
        this.dishSupplyRepository = dishSupplyRepository;
        this.dishRepository = dishRepository;
        this.supplyRepository = supplyRepository;
    }


    public DishSupply addSupplyToDish(Long dishId, Long supplyId, Double quantity) {
        DishEntity dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        SupplyEntity supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new RuntimeException("Supply not found"));

        DishSupply dishSupply = new DishSupply();
        dishSupply.setDish(dish);
        dishSupply.setSupply(supply);
        dishSupply.setQuantity(quantity);

        return dishSupplyRepository.save(dishSupply);
    }

    public void removeSupplyFromDish(Long dishSupplyId) {
        dishSupplyRepository.deleteById(dishSupplyId);
    }
}
