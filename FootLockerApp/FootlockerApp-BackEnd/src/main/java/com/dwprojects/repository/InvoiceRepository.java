package com.dwprojects.repository;

import com.dwprojects.model.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, Integer> {
    @Query("SELECT i FROM Invoices i WHERE i.store.store_id = :storeId")
    List<Invoices> findByStoreId(int storeId);

    @Query("SELECT i FROM Invoices i WHERE i.user.user_id = :userId")
    List<Invoices> findByUserId(int userId);

    @Query("SELECT i FROM Invoices i WHERE i.date BETWEEN :startDate AND :endDate")
    List<Invoices> findByDateBetween(Date startDate, Date endDate);
}
