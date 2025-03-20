package com.dwprojects.controller;

import com.dwprojects.model.Invoices;
import com.dwprojects.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:3000")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public List<Invoices> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoices> getInvoiceById(@PathVariable int id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public List<Invoices> getInvoicesByStore(@PathVariable int storeId) {
        return invoiceService.getInvoicesByStoreId(storeId);
    }

    @GetMapping("/user/{userId}")
    public List<Invoices> getInvoicesByUser(@PathVariable int userId) {
        return invoiceService.getInvoicesByUserId(userId);
    }

    @GetMapping("/date-range")
    public List<Invoices> getInvoicesByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return invoiceService.getInvoicesByDateRange(startDate, endDate);
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@Valid @RequestBody Invoices invoice) {
        if (invoice.getTotal_price() < 0) {
            return ResponseEntity.badRequest().body("Total price cannot be negative");
        }
        return ResponseEntity.ok(invoiceService.saveInvoice(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoices> updateInvoice(@PathVariable int id, @Valid @RequestBody Invoices invoice) {
        return invoiceService.getInvoiceById(id)
                .map(existingInvoice -> {
                    invoice.setInvoice_number(id);
                    return ResponseEntity.ok(invoiceService.saveInvoice(invoice));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable int id) {
        return invoiceService.getInvoiceById(id)
                .map(invoice -> {
                    invoiceService.deleteInvoice(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
