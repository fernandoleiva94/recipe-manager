package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.SupplyLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LossRepository extends JpaRepository<SupplyLoss, Long> {
    List<SupplyLoss> findByLossDate(LocalDate date);
     List<SupplyLoss> findByLossDateBetween(LocalDate from,LocalDate to);
}
