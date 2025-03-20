package com.dwprojects.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sale_id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "Store is required")
    private Stores store;

    @Column(nullable = false)
    @Min(value = 1, message = "Units must be at least 1")
    private int unites;

    @Column(nullable = false)
    @Min(value = 0, message = "Total price cannot be negative")
    private double total_price;

    @ManyToOne
    @JoinColumn(name = "invoice_number", nullable = false)
    @NotNull(message = "Invoice is required")
    private Invoices invoices;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    private Products product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;
}
