package com.dwprojects.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "product_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productType_id;
    
    @Column(nullable = false)
    private String name;
}
