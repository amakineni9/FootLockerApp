package com.dwprojects.controller;

import com.dwprojects.model.Inventory;
import com.dwprojects.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable int id) {
        return inventoryService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public List<Inventory> getInventoryByStore(@PathVariable int storeId) {
        return inventoryService.getInventoryByStore(storeId);
    }

    @GetMapping("/product/{productId}")
    public List<Inventory> getInventoryByProduct(@PathVariable int productId) {
        return inventoryService.getInventoryByProduct(productId);
    }

    @GetMapping("/store/{storeId}/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByStoreAndProduct(
            @PathVariable int storeId,
            @PathVariable int productId) {
        return inventoryService.getInventoryByStoreAndProduct(storeId, productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lowstock")
    public List<Inventory> getAllLowStockItems() {
        return inventoryService.getAllLowStockItems();
    }

    @GetMapping("/lowstock/store/{storeId}")
    public List<Inventory> getLowStockItemsByStore(@PathVariable int storeId) {
        return inventoryService.getLowStockItemsByStore(storeId);
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@Valid @RequestBody Inventory inventory) {
        try {
            return ResponseEntity.ok(inventoryService.saveInventory(inventory));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable int id, @Valid @RequestBody Inventory inventory) {
        return inventoryService.getInventoryById(id)
                .map(existingInventory -> {
                    inventory.setInventory_id(id);
                    try {
                        return ResponseEntity.ok(inventoryService.saveInventory(inventory));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<?> updateQuantity(
            @PathVariable int id,
            @RequestBody Map<String, Integer> update) {
        try {
            Integer quantityChange = update.get("quantityChange");
            if (quantityChange == null) {
                return ResponseEntity.badRequest().body("quantityChange is required");
            }
            return ResponseEntity.ok(inventoryService.updateQuantity(id, quantityChange));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/thresholds")
    public ResponseEntity<?> updateThresholds(
            @PathVariable int id,
            @RequestBody Map<String, Integer> thresholds) {
        try {
            Integer minThreshold = thresholds.get("minThreshold");
            Integer maxThreshold = thresholds.get("maxThreshold");
            if (minThreshold == null || maxThreshold == null) {
                return ResponseEntity.badRequest().body("Both minThreshold and maxThreshold are required");
            }
            return ResponseEntity.ok(inventoryService.updateThresholds(id, minThreshold, maxThreshold));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable int id) {
        return inventoryService.getInventoryById(id)
                .map(inventory -> {
                    inventoryService.deleteInventory(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
