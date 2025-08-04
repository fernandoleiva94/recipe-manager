package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.RecipeLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecipeLossRepository extends JpaRepository<RecipeLoss, Long> {
    List<RecipeLoss> findAllByUserId(Long userId);
    List<RecipeLoss> findAllByUserIdAndLossDateBetween(Long userId, LocalDateTime from, LocalDateTime to);
}
