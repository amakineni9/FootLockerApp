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

import com.dwprojects.model.Stores;
import com.dwprojects.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = StoreController.class)
@ActiveProfiles("test")
public class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    private Stores testStore;
    private List<Stores> storeList;

    @BeforeEach
    void setUp() {
        testStore = new Stores();
        testStore.setStore_id(1);
        testStore.setCity("New York");
        testStore.setState("NY");

        Stores secondStore = new Stores();
        secondStore.setStore_id(2);
        secondStore.setCity("Los Angeles");
        secondStore.setState("CA");

        storeList = new ArrayList<>();
        storeList.add(testStore);
        storeList.add(secondStore);
    }

    @Test
    public void getAllStores_ShouldReturnList() throws Exception {
        given(storeService.getAllStores()).willReturn(storeList);

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].city").value("New York"))
                .andExpect(jsonPath("$[1].city").value("Los Angeles"))
                .andDo(print());
    }

    @Test
    public void getStoreById_WhenExists_ShouldReturnStore() throws Exception {
        given(storeService.getStoreById(1)).willReturn(Optional.of(testStore));

        mockMvc.perform(get("/api/stores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.state").value("NY"))
                .andDo(print());
    }

    @Test
    public void getStoreById_WhenNotExists_ShouldReturn404() throws Exception {
        given(storeService.getStoreById(999)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/stores/999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void createStore_WithValidData_ShouldReturnCreatedStore() throws Exception {
        given(storeService.saveStore(any(Stores.class))).willReturn(testStore);

        mockMvc.perform(post("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStore)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("New York"))
                .andDo(print());
    }

    @Test
    public void createStore_WithInvalidState_ShouldReturn400() throws Exception {
        Stores invalidStore = new Stores();
        invalidStore.setCity("New York");
        invalidStore.setState("NewYork"); // Invalid state code (should be 2 characters)

        mockMvc.perform(post("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidStore)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void updateStore_WhenExists_ShouldReturnUpdatedStore() throws Exception {
        Stores updatedStore = new Stores();
        updatedStore.setStore_id(1);
        updatedStore.setCity("Brooklyn");
        updatedStore.setState("NY");

        given(storeService.getStoreById(1)).willReturn(Optional.of(testStore));
        given(storeService.saveStore(any(Stores.class))).willReturn(updatedStore);

        mockMvc.perform(put("/api/stores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStore)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Brooklyn"))
                .andDo(print());
    }

    @Test
    public void deleteStore_WhenExists_ShouldReturn200() throws Exception {
        given(storeService.getStoreById(1)).willReturn(Optional.of(testStore));
        doNothing().when(storeService).deleteStore(1);

        mockMvc.perform(delete("/api/stores/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getStoresByState_ShouldReturnFilteredList() throws Exception {
        List<Stores> nyStores = List.of(testStore);
        given(storeService.getStoresByState("NY")).willReturn(nyStores);

        mockMvc.perform(get("/api/stores/state/NY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].city").value("New York"))
                .andDo(print());
    }
}
