package com.sevenb.recipes_manager.service;

import com.sevenb.recipes_manager.dto.LossDTO;
import com.sevenb.recipes_manager.dto.SupplyLossOutputDTO;
import com.sevenb.recipes_manager.entity.SupplyLoss;
import com.sevenb.recipes_manager.entity.SupplyEntity;
import com.sevenb.recipes_manager.repository.LossRepository;
import com.sevenb.recipes_manager.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LossService {

    @Autowired
    private LossRepository lossRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    public SupplyLoss registerLoss(LossDTO dto) {
        SupplyEntity supply = supplyRepository.findById(dto.getSupplyId())
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

        // Descontar del stock la cantidad perdida
        if (supply.getStock() == null) {
            supply.setStock(0.0);
        }
        double nuevoStock = supply.getStock() - dto.getLostQuantity();
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente para registrar la pérdida");
        }
        supply.setStock(nuevoStock);
        supplyRepository.save(supply);

        SupplyLoss loss = new SupplyLoss();
        loss.setSupply(supply);
        loss.setLostQuantity(dto.getLostQuantity());
        loss.setDescription(dto.getDescription());
        loss.setLossDate(dto.getLossDate());
        loss.setImageUrl(dto.getImageUrl());

        return lossRepository.save(loss);
    }

    public List<SupplyLossOutputDTO> findByLossDateBetween(LocalDate from,LocalDate to){


        return lossRepository.findByLossDateBetween(from, to)
                .stream().map(this::toSupllylossDto).collect(Collectors.toList());

    }

    public SupplyLossOutputDTO toSupllylossDto(SupplyLoss supplyLoss){
        SupplyLossOutputDTO dto = new SupplyLossOutputDTO();
        dto.setId(supplyLoss.getId());
        dto.setQuantity(supplyLoss.getLostQuantity());
        dto.setDescription(supplyLoss.getDescription());
        dto.setSupplyName(supplyLoss.getSupply().getName());
        dto.setSupplyId(supplyLoss.getSupply().getId());
        dto.setLossDate(supplyLoss.getLossDate());
        dto.setUnit(supplyLoss.getSupply().getUnit());
        dto.setImageUrl(supplyLoss.getImageUrl());
    return dto;
    }


}
