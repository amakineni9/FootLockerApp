package com.dwprojects.repository;

import com.dwprojects.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    @Query("SELECT i FROM Inventory i WHERE i.store.store_id = :storeId")
    List<Inventory> findByStoreId(int storeId);
    
    @Query("SELECT i FROM Inventory i WHERE i.product.product_id = :productId")
    List<Inventory> findByProductId(int productId);
    
    @Query("SELECT i FROM Inventory i WHERE i.store.store_id = :storeId AND i.product.product_id = :productId")
    Optional<Inventory> findByStoreAndProduct(int storeId, int productId);
    
    @Query("SELECT i FROM Inventory i WHERE i.low_stock_alert = true")
    List<Inventory> findAllLowStock();
    
    @Query("SELECT i FROM Inventory i WHERE i.store.store_id = :storeId AND i.low_stock_alert = true")
    List<Inventory> findLowStockByStore(int storeId);
}
