package com.dwprojects.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;
    
    @ManyToOne
    @JoinColumn(name = "productType_id", nullable = false)
    private ProductTypes productType;
    
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brands brand;
    
    @Column(nullable = false)
    private double price;
}
