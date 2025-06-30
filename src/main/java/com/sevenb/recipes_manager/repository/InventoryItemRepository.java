package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    List<InventoryItem> findAllByUserId(Long userId);

}
