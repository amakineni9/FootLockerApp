package com.dwprojects.repository;

import com.dwprojects.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    @Query("SELECT p FROM Products p WHERE p.brand.brand_id = ?1")
    List<Products> findByBrandId(int brandId);

    @Query("SELECT p FROM Products p WHERE p.productType.productType_id = ?1")
    List<Products> findByProductTypeId(int productTypeId);
}
