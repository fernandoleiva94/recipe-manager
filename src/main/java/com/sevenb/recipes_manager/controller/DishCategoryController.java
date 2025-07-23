package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.entity.DishCategory;
import com.sevenb.recipes_manager.entity.RecipeCategory;
import com.sevenb.recipes_manager.service.DishCategoryService;
import com.sevenb.recipes_manager.service.DishService;
import com.sevenb.recipes_manager.service.RecipeCategoryService;
import com.sevenb.recipes_manager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/dish-categories")
public class DishCategoryController {

        @Autowired
        private DishCategoryService service;

        @Autowired
        private JwtUtil jwtUtil;

        @GetMapping
        public List<DishCategory> getAll(@RequestHeader("Authorization") String authHeader) {
            Long userId = extractUserId(authHeader);
            return service.getAllByUser(userId);
        }

        @PostMapping
        public DishCategory create(@RequestHeader("Authorization") String authHeader,
                                     @RequestBody DishCategory category) {
            Long userId = extractUserId(authHeader);
            category.setUserId(userId);
            return service.create(category);
        }

        @PutMapping("/{id}")
        public DishCategory update(@RequestHeader("Authorization") String authHeader,
                                     @PathVariable Long id,
                                     @RequestBody DishCategory updated) {
            Long userId = extractUserId(authHeader);
            return service.update(id, updated, userId);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long id) {
            Long userId = extractUserId(authHeader);
            service.delete(id, userId);
            return ResponseEntity.noContent().build();
        }

        private Long extractUserId(String authHeader) {
            String token = authHeader.replace("Bearer ", "");
            return jwtUtil.extractUserId(token);
        }

}
