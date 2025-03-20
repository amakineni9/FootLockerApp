package com.dwprojects.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.dwprojects.model.Inventory;
import com.dwprojects.model.Products;
import com.dwprojects.model.Stores;
import com.dwprojects.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = InventoryController.class)
@ActiveProfiles("test")
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    private Inventory testInventory;
    private List<Inventory> inventoryList;
    private Stores testStore;
    private Products testProduct;

    @BeforeEach
    void setUp() {
        testStore = new Stores();
        testStore.setStore_id(1);
        testStore.setCity("New York");
        testStore.setState("NY");

        testProduct = new Products();
        testProduct.setProduct_id(1);
        testProduct.setPrice(129.99);

        testInventory = new Inventory();
        testInventory.setInventory_id(1);
        testInventory.setStore(testStore);
        testInventory.setProduct(testProduct);
        testInventory.setQuantity(25);
        testInventory.setMin_threshold(10);
        testInventory.setMax_threshold(50);
        testInventory.setLow_stock_alert(false);

        Inventory lowStockInventory = new Inventory();
        lowStockInventory.setInventory_id(2);
        lowStockInventory.setStore(testStore);
        lowStockInventory.setProduct(testProduct);
        lowStockInventory.setQuantity(5);
        lowStockInventory.setMin_threshold(10);
        lowStockInventory.setMax_threshold(50);
        lowStockInventory.setLow_stock_alert(true);

        inventoryList = new ArrayList<>();
        inventoryList.add(testInventory);
        inventoryList.add(lowStockInventory);
    }

    @Test
    public void getAllInventory_ShouldReturnList() throws Exception {
        given(inventoryService.getAllInventory()).willReturn(inventoryList);

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].quantity").value(25))
                .andExpect(jsonPath("$[1].quantity").value(5))
                .andDo(print());
    }

    @Test
    public void getInventoryById_WhenExists_ShouldReturnInventory() throws Exception {
        given(inventoryService.getInventoryById(1)).willReturn(Optional.of(testInventory));

        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(25))
                .andExpect(jsonPath("$.store.city").value("New York"))
                .andDo(print());
    }

    @Test
    public void getInventoryByStore_ShouldReturnFilteredList() throws Exception {
        given(inventoryService.getInventoryByStore(1)).willReturn(inventoryList);

        mockMvc.perform(get("/api/inventory/store/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].store.city").value("New York"))
                .andDo(print());
    }

    @Test
    public void getLowStockItems_ShouldReturnFilteredList() throws Exception {
        List<Inventory> lowStockList = List.of(inventoryList.get(1));
        given(inventoryService.getAllLowStockItems()).willReturn(lowStockList);

        mockMvc.perform(get("/api/inventory/lowstock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].low_stock_alert").value(true))
                .andDo(print());
    }

    @Test
    public void updateQuantity_ValidChange_ShouldReturnUpdatedInventory() throws Exception {
        Inventory updatedInventory = new Inventory();
        updatedInventory.setInventory_id(1);
        updatedInventory.setStore(testStore);
        updatedInventory.setProduct(testProduct);
        updatedInventory.setQuantity(30); // Increased by 5
        updatedInventory.setMin_threshold(10);
        updatedInventory.setMax_threshold(50);
        updatedInventory.setLow_stock_alert(false);

        given(inventoryService.updateQuantity(1, 5)).willReturn(updatedInventory);

        String requestBody = objectMapper.writeValueAsString(Map.of("quantityChange", 5));

        mockMvc.perform(patch("/api/inventory/1/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(30))
                .andDo(print());
    }

    @Test
    public void updateQuantity_InsufficientStock_ShouldReturn400() throws Exception {
        given(inventoryService.updateQuantity(1, -30))
                .willThrow(new IllegalArgumentException("Insufficient stock available"));

        String requestBody = objectMapper.writeValueAsString(Map.of("quantityChange", -30));

        mockMvc.perform(patch("/api/inventory/1/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Insufficient stock available"))
                .andDo(print());
    }

    @Test
    public void updateThresholds_ValidValues_ShouldReturnUpdatedInventory() throws Exception {
        Inventory updatedInventory = new Inventory();
        updatedInventory.setInventory_id(1);
        updatedInventory.setStore(testStore);
        updatedInventory.setProduct(testProduct);
        updatedInventory.setQuantity(25);
        updatedInventory.setMin_threshold(15);
        updatedInventory.setMax_threshold(60);
        updatedInventory.setLow_stock_alert(false);

        given(inventoryService.updateThresholds(1, 15, 60)).willReturn(updatedInventory);

        String requestBody = objectMapper.writeValueAsString(Map.of(
            "minThreshold", 15,
            "maxThreshold", 60
        ));

        mockMvc.perform(patch("/api/inventory/1/thresholds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min_threshold").value(15))
                .andExpect(jsonPath("$.max_threshold").value(60))
                .andDo(print());
    }

    @Test
    public void updateThresholds_InvalidValues_ShouldReturn400() throws Exception {
        given(inventoryService.updateThresholds(1, 20, 10))
                .willThrow(new IllegalArgumentException("Invalid threshold values"));

        String requestBody = objectMapper.writeValueAsString(Map.of(
            "minThreshold", 20,
            "maxThreshold", 10
        ));

        mockMvc.perform(patch("/api/inventory/1/thresholds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid threshold values"))
                .andDo(print());
    }
}
