package com.sevenb.recipes_manager.service;


import com.sevenb.recipes_manager.entity.InventoryItem;
import com.sevenb.recipes_manager.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryItemService {

    private final InventoryItemRepository repository;

    public InventoryItemService(InventoryItemRepository repository) {
        this.repository = repository;
    }

    public List<InventoryItem> findAllByIdUser(Long userId) {
        return repository.findAll();
    }

    public Optional<InventoryItem> findById(Long id) {
        return repository.findById(id);
    }

    public InventoryItem save(InventoryItem item) {
        return repository.save(item);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
