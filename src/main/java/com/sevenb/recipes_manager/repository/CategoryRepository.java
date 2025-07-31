package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.SupplyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<SupplyCategory,Long> {

    Optional<SupplyCategory> findByDescriptionAndUserId(String name, Long userId);
    List<SupplyCategory> findAllByUserId(Long userId);

}
