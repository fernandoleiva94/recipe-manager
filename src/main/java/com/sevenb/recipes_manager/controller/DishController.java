package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.DishDto;
import com.sevenb.recipes_manager.dto.DishOutpuDto;
import com.sevenb.recipes_manager.entity.DishEntity;
import com.sevenb.recipes_manager.service.CloudinaryService;
import com.sevenb.recipes_manager.service.DishService;
import com.sevenb.recipes_manager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {


    private final DishService dishService;
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;



    @GetMapping
    public ResponseEntity<List<DishOutpuDto>> getAllDishes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.status(HttpStatus.OK).body(dishService.getAllDishes(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishOutpuDto> getDishById(@PathVariable Long id) {
        DishOutpuDto dishOutpuDto = dishService.getDishById(id);
        if(Objects.nonNull(dishOutpuDto))
            return ResponseEntity.ok(dishOutpuDto);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DishEntity> createDish(@RequestHeader("Authorization") String authHeader,
                                                 @RequestPart(value = "image", required = false) MultipartFile image,
                                                 @RequestPart("data") DishDto dish) throws IOException {
        String token = authHeader.replace("Bearer ", "");

        if (image != null) {
            String url = cloudinaryService.upload(image);
            dish.setImageUrl(url);
        }

        Long userId = jwtUtil.extractUserId(token);
        dish.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dishService.createDish(dish));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishOutpuDto> updateDish(@PathVariable Long id, @RequestBody DishDto dish) {
        return ResponseEntity.ok(dishService.updateDish(id, dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
