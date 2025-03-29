package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.DishDto;
import com.sevenb.recipes_manager.dto.DishOutpuDto;
import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {


    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }


    @GetMapping
    public List<DishOutpuDto> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishEntity> getDishById(@PathVariable Long id) {
        return dishService.getDishById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DishEntity> createDish(@RequestBody DishDto dish) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishService.createDish(dish));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishEntity> updateDish(@PathVariable Long id, @RequestBody DishEntity dish) {
        return ResponseEntity.ok(dishService.updateDish(id, dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
