package com.dwprojects.service;

import com.dwprojects.model.Invoices;
import com.dwprojects.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Cacheable("invoices")
    public List<Invoices> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    @Cacheable(value = "invoices", key = "#id")
    public Optional<Invoices> getInvoiceById(int id) {
        return invoiceRepository.findById(id);
    }
    
    @Cacheable(value = "invoices", key = "'store:' + #storeId")
    public List<Invoices> getInvoicesByStoreId(int storeId) {
        return invoiceRepository.findByStoreId(storeId);
    }
    
    @Cacheable(value = "invoices", key = "'user:' + #userId")
    public List<Invoices> getInvoicesByUserId(int userId) {
        return invoiceRepository.findByUserId(userId);
    }
    
    @Cacheable(value = "invoices", key = "'date:' + #startDate + ':' + #endDate")
    public List<Invoices> getInvoicesByDateRange(Date startDate, Date endDate) {
        return invoiceRepository.findByDateBetween(startDate, endDate);
    }
    
    @CacheEvict(value = "invoices", allEntries = true)
    public Invoices saveInvoice(Invoices invoice) {
        if (invoice.getDate() == null) {
            invoice.setDate(new Date());
        }
        return invoiceRepository.save(invoice);
    }
    
    @CacheEvict(value = "invoices", allEntries = true)
    public void deleteInvoice(int id) {
        invoiceRepository.deleteById(id);
    }
}
