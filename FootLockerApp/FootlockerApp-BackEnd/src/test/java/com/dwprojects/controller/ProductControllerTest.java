package com.dwprojects.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.dwprojects.model.Products;
import com.dwprojects.model.Brands;
import com.dwprojects.model.ProductTypes;
import com.dwprojects.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProductController.class)
@ActiveProfiles("test")
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Products testProduct;
    private List<Products> productList;
    private Brands testBrand;
    private ProductTypes testType;

    @BeforeEach
    void setUp() {
        testBrand = new Brands();
        testBrand.setBrand_id(1);
        testBrand.setName("Nike");

        testType = new ProductTypes();
        testType.setProductType_id(1);
        testType.setName("Running");

        testProduct = new Products();
        testProduct.setProduct_id(1);
        testProduct.setBrand(testBrand);
        testProduct.setProductType(testType);
        testProduct.setPrice(129.99);

        Products secondProduct = new Products();
        secondProduct.setProduct_id(2);
        secondProduct.setBrand(testBrand);
        secondProduct.setProductType(testType);
        secondProduct.setPrice(149.99);

        productList = new ArrayList<>();
        productList.add(testProduct);
        productList.add(secondProduct);
    }

    @Test
    public void getAllProducts_ShouldReturnList() throws Exception {
        given(productService.getAllProducts()).willReturn(productList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].price").value(129.99))
                .andExpect(jsonPath("$[1].price").value(149.99))
                .andDo(print());
    }

    @Test
    public void getProductById_WhenExists_ShouldReturnProduct() throws Exception {
        given(productService.getProductById(1)).willReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(129.99))
                .andExpect(jsonPath("$.brand.name").value("Nike"))
                .andDo(print());
    }

    @Test
    public void getProductsByBrandId_ShouldReturnList() throws Exception {
        given(productService.getProductsByBrandId(1)).willReturn(productList);

        mockMvc.perform(get("/api/products/brand/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].brand.name").value("Nike"))
                .andDo(print());
    }

    @Test
    public void getProductsByTypeId_ShouldReturnList() throws Exception {
        given(productService.getProductsByTypeId(1)).willReturn(productList);

        mockMvc.perform(get("/api/products/type/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].productType.name").value("Running"))
                .andDo(print());
    }

    @Test
    public void createProduct_ShouldReturnCreatedProduct() throws Exception {
        given(productService.saveProduct(any(Products.class))).willReturn(testProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(129.99))
                .andDo(print());
    }

    @Test
    public void updateProduct_WhenExists_ShouldReturnUpdatedProduct() throws Exception {
        Products updatedProduct = new Products();
        updatedProduct.setProduct_id(1);
        updatedProduct.setBrand(testBrand);
        updatedProduct.setProductType(testType);
        updatedProduct.setPrice(159.99);

        given(productService.getProductById(1)).willReturn(Optional.of(testProduct));
        given(productService.saveProduct(any(Products.class))).willReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(159.99))
                .andDo(print());
    }

    @Test
    public void deleteProduct_WhenExists_ShouldReturn200() throws Exception {
        given(productService.getProductById(1)).willReturn(Optional.of(testProduct));
        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
