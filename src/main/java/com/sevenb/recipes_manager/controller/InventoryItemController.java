package com.sevenb.recipes_manager.controller;


import com.sevenb.recipes_manager.entity.InventoryItem;
import com.sevenb.recipes_manager.service.CloudinaryService;
import com.sevenb.recipes_manager.service.InventoryItemService;
import com.sevenb.recipes_manager.util.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryItemController {

    private final InventoryItemService service;
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;


    public InventoryItemController(InventoryItemService service, JwtUtil jwtUtil, CloudinaryService cloudinaryService) {
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public List<InventoryItem> getAll(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);




        return service.findAllByIdUser(userId);
    }

    @GetMapping("/{id}")
    public InventoryItem getById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
    }

    @PostMapping
    public InventoryItem create(@RequestHeader("Authorization") String authHeader,
                                @RequestPart("inventoryItem") InventoryItem item,
                                @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        if (image != null) {
            String url = cloudinaryService.upload(image);
            item.setImageUrl(url);
        }

        item.setUserId(userId);
        return service.save(item);
    }

    @PutMapping("/{id}")
    public InventoryItem update(@PathVariable Long id, @RequestBody InventoryItem item) {
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
