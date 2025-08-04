package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.LossDTO;
import com.sevenb.recipes_manager.dto.SupplyLossOutputDTO;
import com.sevenb.recipes_manager.entity.StockMovement;
import com.sevenb.recipes_manager.entity.SupplyLoss;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.service.CloudinaryService;
import com.sevenb.recipes_manager.service.LossService;
import com.sevenb.recipes_manager.service.SupplyService;
import com.sevenb.recipes_manager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@RequiredArgsConstructor
public class SupplyController {


    private final SupplyService supplyService;
    private final JwtUtil jwtUtil;
    private final LossService lossService;
    private final CloudinaryService cloudinaryService;

    // Get all supplies
    @GetMapping
    public List<SupplyEntity> getAllSupplies(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        return supplyService.getAllSupplies(userId);
    }

    // Get a supply by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplyEntity> getSupplyById(
            @PathVariable Long id) {
        SupplyEntity supply = supplyService.getSupplyById(id);
        if (supply != null) {
            return ResponseEntity.ok(supply);
        }
        return ResponseEntity.notFound().build();
    }

    // Create a new supply
    @PostMapping
    public SupplyEntity createSupply(@RequestHeader("Authorization") String authHeader
            ,@RequestBody SupplyEntity supply) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        supply.setUserId(userId);
        return supplyService.saveSupply(supply);
    }

    // Update an existing supply
    @PutMapping("/{id}")
    public ResponseEntity<SupplyEntity> updateSupply(@PathVariable Long id, @RequestBody SupplyEntity supplyDetails) {
        SupplyEntity updatedSupply = supplyService.updateSupply(id, supplyDetails);
        if (updatedSupply != null) {
            return ResponseEntity.ok(updatedSupply);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a supply by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }

    // Search supplies by name
    @GetMapping("/search")
    public List<SupplyEntity> searchSupplies(@RequestParam String name) {
        return supplyService.searchByName(name);
    }

    @PostMapping("/losses")
    public ResponseEntity<SupplyLoss> createLoss(@RequestPart("loss") LossDTO dto,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        if (image != null) {
            String url = cloudinaryService.upload(image);
            dto.setImageUrl(url);
        }


        SupplyLoss saved = lossService.registerLoss(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/losses")
    public List<SupplyLossOutputDTO> getLossesByDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return lossService.findByLossDateBetween(from, to);
    }

    @PutMapping("/{id}/stock/increase")
    public ResponseEntity<SupplyEntity> increaseStock(
            @PathVariable Long id,
            @RequestParam double amount,
            @RequestParam(required = false, defaultValue = "Compra") String reason
    ) {
        return ResponseEntity.of(supplyService.adjustStock(id, amount, "ENTRADA", reason));
    }

    @PutMapping("/{id}/stock/decrease")
    public ResponseEntity<SupplyEntity> decreaseStock(
            @PathVariable Long id,
            @RequestParam double amount,
            @RequestParam(required = false, defaultValue = "Consumo") String reason
    ) {
        return ResponseEntity.of(supplyService.adjustStock(id, -amount, "SALIDA", reason));
    }

    @GetMapping("/low-stock")
    public List<SupplyEntity> getLowStock(@RequestHeader("Authorization") String authHeader,
                                          @RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "to", required = false) String to) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        if (from != null && to != null) {
            return supplyService.getLowStockSuppliesByUserAndCheckStockAndDate(userId, from, to);
        }
        return supplyService.getLowStockSuppliesByUserAndCheckStock(userId);
    }

    @GetMapping("/{id}/movements")
    public List<StockMovement> getMovements(@PathVariable Long id) {
        return supplyService.getMovementsBySupply(id);
    }

    @GetMapping("/all-stock")
    public List<SupplyEntity> getAllSuppliesCheckStock(@RequestHeader("Authorization") String authHeader,
                                                       @RequestParam(required = false, defaultValue = "true") boolean checkStock) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        return supplyService.getAllSuppliesCheckStock(userId, checkStock);
    }



}
