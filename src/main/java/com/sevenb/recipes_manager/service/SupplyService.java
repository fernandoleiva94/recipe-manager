package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.Exception.CannotDeleteSupplyException;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class SupplyService {

    @Autowired
    private SupplyRepository supplyRepository;



    public List<SupplyEntity> getAllSupplies(Long userId) {
        return supplyRepository.findAllByUserId(userId);
    }

    public SupplyEntity getSupplyById(Long id) {
        return supplyRepository.findById(id).orElse(null);
    }

    public SupplyEntity saveSupply(SupplyEntity supply) {
        supply.setName(supply.getName().toUpperCase(Locale.ROOT));
        return supplyRepository.save(supply);
    }

    public SupplyEntity updateSupply(Long id, SupplyEntity supplyDetails) {
        SupplyEntity supply = supplyRepository.findById(id).orElse(null);
        if (supply != null) {
            supply.setName(supplyDetails.getName().toUpperCase(Locale.ROOT));
            supply.setQuantity(supplyDetails.getQuantity());
            supply.setPrice(supplyDetails.getPrice());
            supply.setUnit(supplyDetails.getUnit());
            supply.setDescription(supplyDetails.getDescription());
            supply.setWastage(supplyDetails.getWastage());
            supply.setCategory(supplyDetails.getCategory());

            updateRecipe();//hacer que el supplie informa a recipes de la actualizacion de un insumo
            return supplyRepository.save(supply);
        }
        return null;
    }

    private void updateRecipe(){

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
}