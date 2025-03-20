package com.dwprojects.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "stores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int store_id;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false, length = 2)
    @Size(min = 2, max = 2, message = "State code must be exactly 2 characters")
    private String state;
}
