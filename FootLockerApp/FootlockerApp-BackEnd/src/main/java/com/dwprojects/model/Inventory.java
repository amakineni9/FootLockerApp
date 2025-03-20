package com.dwprojects.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "inventory",
       uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "product_id"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inventory_id;
    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "Store is required")
    private Stores store;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    private Products product;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Minimum threshold cannot be negative")
    private int min_threshold;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Maximum threshold cannot be negative")
    private int max_threshold;
    
    @Column(nullable = false)
    private boolean low_stock_alert;
    
    @PrePersist
    @PreUpdate
    protected void updateLowStockAlert() {
        this.low_stock_alert = this.quantity <= this.min_threshold;
    }
    
    public void checkAndUpdateLowStockAlert() {
        updateLowStockAlert();
    }
}
