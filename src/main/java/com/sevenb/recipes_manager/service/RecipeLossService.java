package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.dto.recipe.RecipeLossDto;
import com.sevenb.recipes_manager.entity.Recipe;
import com.sevenb.recipes_manager.entity.RecipeLoss;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.RecipeLossRepository;
import com.sevenb.recipes_manager.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
public class RecipeLossService {
    private final RecipeLossRepository recipeLossRepository;
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;

    public RecipeLossService(RecipeLossRepository recipeLossRepository,
                             RecipeRepository recipeRepository,
                             CloudinaryService cloudinaryService) {
        this.recipeLossRepository = recipeLossRepository;
        this.recipeRepository = recipeRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public RecipeLoss registerLoss(Long recipeId, Double recipeQuantity,
                                   Long userId, String notes,
                                   String imageBase64) throws IOException {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        double qty = recipeQuantity != null ? recipeQuantity : 1.0;


        RecipeLoss loss = new RecipeLoss();
        loss.setRecipe(recipe);
        loss.setQuantityLost(qty);
        loss.setUnit(recipe.getUnit());
        loss.setLossDate(LocalDateTime.now());
        loss.setUserId(userId);
        loss.setNotes(notes);

        if ( imageBase64!= null && !imageBase64.isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
            String url = cloudinaryService.upload(imageBytes);
            loss.setImageUrl(url);
        }

        // Descontar stock de insumos con checkStock=true en base a la proporción de receta perdida
        if (recipe.getRecipeSupplies() != null) {
            double proporcion = qty / recipe.getQuantity(); // proporción de receta perdida respecto a la receta base
            for (var recipeSupply : recipe.getRecipeSupplies()) {
                SupplyEntity supply = recipeSupply.getSupply();
                if (supply.isCheckStock() && supply.getStock() != null) {
                    // Calcular insumo a descontar según la proporción
                    double cantidadDescontar = recipeSupply.getQuantity() * proporcion;
                    double nuevoStock = supply.getStock() - cantidadDescontar;
                    supply.setStock(nuevoStock);
                }
            }
        }

        return recipeLossRepository.save(loss);
    }

    public List<RecipeLoss> getAllLossesByUser(Long userId) {
        return recipeLossRepository.findAllByUserId(userId);
    }

    public RecipeLossDto toDto(RecipeLoss loss) {
        RecipeLossDto dto = new RecipeLossDto();
        dto.setId(loss.getId());
        dto.setRecipeId(loss.getRecipe().getId());
        dto.setRecipeName(loss.getRecipe().getName());
        dto.setQuantityLost(loss.getQuantityLost());
        dto.setUnit(loss.getUnit());
        dto.setLossDate(loss.getLossDate());
        dto.setNotes(loss.getNotes());
        dto.setImageUrl(loss.getImageUrl());
        return dto;
    }

    public List<RecipeLossDto> getAllLossesByUserDto(Long userId) {
        return recipeLossRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<RecipeLossDto> getAllLossesByUserAndDateRangeDto(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fromDate = LocalDate.parse(from, formatter).atStartOfDay();
        LocalDateTime toDate = LocalDate.parse(to, formatter).atTime(23, 59, 59);
        return recipeLossRepository.findAllByUserIdAndLossDateBetween(userId, fromDate, toDate)
                .stream()
                .map(this::toDto)
                .toList();
    }
}
