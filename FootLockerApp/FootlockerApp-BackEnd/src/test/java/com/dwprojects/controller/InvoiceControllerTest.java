package com.dwprojects.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Date;
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

import com.dwprojects.model.Invoices;
import com.dwprojects.model.Stores;
import com.dwprojects.model.User;
import com.dwprojects.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = InvoiceController.class)
@ActiveProfiles("test")
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvoiceService invoiceService;

    private Invoices testInvoice;
    private List<Invoices> invoiceList;
    private Stores testStore;
    private User testUser;

    @BeforeEach
    void setUp() {
        testStore = new Stores();
        testStore.setStore_id(1);
        testStore.setCity("New York");
        testStore.setState("NY");

        testUser = new User("password123", "test@gmail.com", "John", "Doe", false);
        testUser.setUser_id(1);

        testInvoice = new Invoices();
        testInvoice.setInvoice_number(1);
        testInvoice.setStore(testStore);
        testInvoice.setTotal_price(299.99);
        testInvoice.setDate(new Date());
        testInvoice.setUser(testUser);

        Invoices secondInvoice = new Invoices();
        secondInvoice.setInvoice_number(2);
        secondInvoice.setStore(testStore);
        secondInvoice.setTotal_price(199.99);
        secondInvoice.setDate(new Date());
        secondInvoice.setUser(testUser);

        invoiceList = new ArrayList<>();
        invoiceList.add(testInvoice);
        invoiceList.add(secondInvoice);
    }

    @Test
    public void getAllInvoices_ShouldReturnList() throws Exception {
        given(invoiceService.getAllInvoices()).willReturn(invoiceList);

        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].total_price").value(299.99))
                .andExpect(jsonPath("$[1].total_price").value(199.99))
                .andDo(print());
    }

    @Test
    public void getInvoiceById_WhenExists_ShouldReturnInvoice() throws Exception {
        given(invoiceService.getInvoiceById(1)).willReturn(Optional.of(testInvoice));

        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_price").value(299.99))
                .andExpect(jsonPath("$.store.city").value("New York"))
                .andDo(print());
    }

    @Test
    public void getInvoicesByStore_ShouldReturnFilteredList() throws Exception {
        given(invoiceService.getInvoicesByStoreId(1)).willReturn(invoiceList);

        mockMvc.perform(get("/api/invoices/store/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].store.city").value("New York"))
                .andDo(print());
    }

    @Test
    public void getInvoicesByUser_ShouldReturnFilteredList() throws Exception {
        given(invoiceService.getInvoicesByUserId(1)).willReturn(invoiceList);

        mockMvc.perform(get("/api/invoices/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].user.email").value("test@gmail.com"))
                .andDo(print());
    }

    @Test
    public void createInvoice_WithValidData_ShouldReturnCreatedInvoice() throws Exception {
        given(invoiceService.saveInvoice(any(Invoices.class))).willReturn(testInvoice);

        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testInvoice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_price").value(299.99))
                .andDo(print());
    }

    @Test
    public void createInvoice_WithNegativeTotal_ShouldReturn400() throws Exception {
        testInvoice.setTotal_price(-50.0);

        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testInvoice)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void updateInvoice_WhenExists_ShouldReturnUpdatedInvoice() throws Exception {
        Invoices updatedInvoice = new Invoices();
        updatedInvoice.setInvoice_number(1);
        updatedInvoice.setStore(testStore);
        updatedInvoice.setTotal_price(399.99);
        updatedInvoice.setDate(new Date());
        updatedInvoice.setUser(testUser);

        given(invoiceService.getInvoiceById(1)).willReturn(Optional.of(testInvoice));
        given(invoiceService.saveInvoice(any(Invoices.class))).willReturn(updatedInvoice);

        mockMvc.perform(put("/api/invoices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_price").value(399.99))
                .andDo(print());
    }

    @Test
    public void deleteInvoice_WhenExists_ShouldReturn200() throws Exception {
        given(invoiceService.getInvoiceById(1)).willReturn(Optional.of(testInvoice));
        doNothing().when(invoiceService).deleteInvoice(1);

        mockMvc.perform(delete("/api/invoices/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getInvoicesByDateRange_ShouldReturnFilteredList() throws Exception {
        given(invoiceService.getInvoicesByDateRange(any(Date.class), any(Date.class)))
                .willReturn(invoiceList);

        mockMvc.perform(get("/api/invoices/date-range")
                .param("startDate", "2025-03-12")
                .param("endDate", "2025-03-19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].total_price").value(299.99))
                .andDo(print());
    }
}
