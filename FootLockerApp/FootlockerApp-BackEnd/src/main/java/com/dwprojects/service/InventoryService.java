package com.dwprojects.service;

import com.dwprojects.model.Inventory;
import com.dwprojects.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Cacheable("inventory")
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    @Cacheable(value = "inventory", key = "#id")
    public Optional<Inventory> getInventoryById(int id) {
        return inventoryRepository.findById(id);
    }
    
    @Cacheable(value = "inventory", key = "'store:' + #storeId")
    public List<Inventory> getInventoryByStore(int storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }
    
    @Cacheable(value = "inventory", key = "'product:' + #productId")
    public List<Inventory> getInventoryByProduct(int productId) {
        return inventoryRepository.findByProductId(productId);
    }
    
    @Cacheable(value = "inventory", key = "'store:' + #storeId + ':product:' + #productId")
    public Optional<Inventory> getInventoryByStoreAndProduct(int storeId, int productId) {
        return inventoryRepository.findByStoreAndProduct(storeId, productId);
    }
    
    @Cacheable(value = "inventory", key = "'lowstock'")
    public List<Inventory> getAllLowStockItems() {
        return inventoryRepository.findAllLowStock();
    }
    
    @Cacheable(value = "inventory", key = "'lowstock:store:' + #storeId")
    public List<Inventory> getLowStockItemsByStore(int storeId) {
        return inventoryRepository.findLowStockByStore(storeId);
    }
    
    @CacheEvict(value = "inventory", allEntries = true)
    public Inventory saveInventory(Inventory inventory) {
        // Ensure low_stock_alert is set based on quantity and threshold
        inventory.checkAndUpdateLowStockAlert();
        return inventoryRepository.save(inventory);
    }
    
    @Transactional
    @CacheEvict(value = "inventory", allEntries = true)
    public Inventory updateQuantity(int inventoryId, int quantityChange) {
        return getInventoryById(inventoryId)
                .map(inventory -> {
                    int newQuantity = inventory.getQuantity() + quantityChange;
                    if (newQuantity < 0) {
                        throw new IllegalArgumentException("Insufficient stock available");
                    }
                    inventory.setQuantity(newQuantity);
                    inventory.checkAndUpdateLowStockAlert();
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
    }
    
    @Transactional
    @CacheEvict(value = "inventory", allEntries = true)
    public Inventory updateThresholds(int inventoryId, int minThreshold, int maxThreshold) {
        if (minThreshold < 0 || maxThreshold < minThreshold) {
            throw new IllegalArgumentException("Invalid threshold values");
        }
        
        return getInventoryById(inventoryId)
                .map(inventory -> {
                    inventory.setMin_threshold(minThreshold);
                    inventory.setMax_threshold(maxThreshold);
                    inventory.checkAndUpdateLowStockAlert();
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
    }
    
    @CacheEvict(value = "inventory", allEntries = true)
    public void deleteInventory(int id) {
        inventoryRepository.deleteById(id);
    }
}
