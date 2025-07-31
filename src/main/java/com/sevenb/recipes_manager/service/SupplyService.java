package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.Exception.CannotDeleteSupplyException;
import com.sevenb.recipes_manager.entity.StockMovement;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.StockMovementRepository;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SupplyService {

    private final SupplyRepository supplyRepository;
    private final StockMovementRepository movementRepository;


    public SupplyService (SupplyRepository supplyRepository, StockMovementRepository stockMovementRepository) {
        this.supplyRepository = supplyRepository;
        this.movementRepository = stockMovementRepository;
    }

    public List<SupplyEntity> getAllSupplies(Long userId) {
        return supplyRepository.findAllByUserId(userId);
    }

    public List<SupplyEntity> getAllSuppliesCheckStock(Long userId, boolean checkStock) {
        return supplyRepository.findAllByUserIdAndCheckStock(userId, checkStock);
    }


    public SupplyEntity getSupplyById(Long id) {
        return supplyRepository.findById(id).orElse(null);
    }

    public SupplyEntity saveSupply(SupplyEntity supply) {
        supply.setName(supply.getName().toUpperCase(Locale.ROOT));
        return supplyRepository.save(supply);
    }

    public SupplyEntity updateSupply(Long id, SupplyEntity supplyDetails) {
        SupplyEntity supply = supplyRepository.findById(id).orElseThrow(() -> new RuntimeException("Supply not found with id: " + id));
            supply.setName(supplyDetails.getName().toUpperCase(Locale.ROOT));
            supply.setQuantity(supplyDetails.getQuantity());
            supply.setPrice(supplyDetails.getPrice());
            supply.setUnit(supplyDetails.getUnit());
            supply.setDescription(supplyDetails.getDescription());
            supply.setWastage(supplyDetails.getWastage());
            supply.setCategory(supplyDetails.getCategory());
            supply.setMinStock(supplyDetails.getMinStock());
            supply.setMaxStock(supplyDetails.getMaxStock());
            supply.setCheckStock(supplyDetails.isCheckStock());
         return supplyRepository.save(supply);
    }


    public void deleteSupply(Long id) {
try{
    supplyRepository.deleteById(id);
  } catch (DataIntegrityViolationException e) {
        // Esto atrapa errores de constraint (relaciones con productos)
        throw new CannotDeleteSupplyException("El insumo está siendo ocupado por algún producto o producto final. Por favor, elimínalos primero.");
    }

    }

    public List<SupplyEntity> searchByName(String name) {
        return supplyRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<SupplyEntity> adjustStock(Long id, double quantity, String type, String reason) {
        return supplyRepository.findById(id).map(s -> {
            if (s.getStock() == null) {
                s.setStock(0.0);
            }
            double newStock = s.getStock() + quantity;
            if (newStock < 0) throw new IllegalArgumentException("Stock insuficiente");
            s.setStock(newStock);
            if (type.equalsIgnoreCase("ENTRADA")) {
                s.setLastRestocked(LocalDateTime.now());
            }
            supplyRepository.save(s);

            // registrar movimiento
            StockMovement movement = new StockMovement();
            movement.setSupply(s);
            movement.setDate(LocalDateTime.now());
            movement.setQuantity(Math.abs(quantity));
            movement.setType(type);
            movement.setReason(reason);
            movementRepository.save(movement);

            return s;
        });
    }

    public List<SupplyEntity> getLowStockSupplies() {
        return supplyRepository.findLowStockSupplies();
    }

    public List<StockMovement> getMovementsBySupply(Long supplyId) {
        return movementRepository.findBySupplyId(supplyId);
    }


}