package com.sevenb.recipes_manager.controller;

import com.sevenb.recipes_manager.dto.LossDTO;
import com.sevenb.recipes_manager.dto.SupplyLossOutputDTO;
import com.sevenb.recipes_manager.entity.SupplyLoss;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.service.LossService;
import com.sevenb.recipes_manager.service.SupplyService;
import com.sevenb.recipes_manager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@RequiredArgsConstructor
public class SupplyController {


    private final SupplyService supplyService;
    private final JwtUtil jwtUtil;
    private final LossService lossService;

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
    public ResponseEntity<SupplyLoss> createLoss(@RequestBody LossDTO dto) {
        SupplyLoss saved = lossService.registerLoss(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/losses")
    public List<SupplyLossOutputDTO> getLossesByDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return lossService.findByLossDateBetween(from, to);
    }


}
