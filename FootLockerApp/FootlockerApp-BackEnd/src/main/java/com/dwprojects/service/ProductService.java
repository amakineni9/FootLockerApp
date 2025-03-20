package com.dwprojects.service;

import com.dwprojects.model.Products;
import com.dwprojects.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @Cacheable("products")
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Cacheable(value = "products", key = "#id")
    public Optional<Products> getProductById(int id) {
        return productRepository.findById(id);
    }
    
    @Cacheable(value = "products", key = "'type:' + #typeId")
    public List<Products> getProductsByType(int typeId) {
        return productRepository.findByProductTypeId(typeId);
    }
    
    @Cacheable(value = "products", key = "'brand:' + #brandId")
    public List<Products> getProductsByBrand(int brandId) {
        return productRepository.findByBrandId(brandId);
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public Products saveProduct(Products product) {
        return productRepository.save(product);
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
