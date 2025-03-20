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

import com.dwprojects.model.User;
import com.dwprojects.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        testUser = new User("password123", "test@gmail.com", "John", "Doe", false);
        testUser.setUser_id(1);

        userList = new ArrayList<>();
        userList.add(testUser);
        userList.add(new User("password123", "jane@gmail.com", "Jane", "Smith", false));
    }

    @Test
    public void getAllUsers_ShouldReturnList() throws Exception {
        given(userService.getAllUsers()).willReturn(userList);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$[1].email").value("jane@gmail.com"))
                .andDo(print());
    }

    @Test
    public void getUserById_WhenExists_ShouldReturnUser() throws Exception {
        given(userService.getUserById(1)).willReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andDo(print());
    }

    @Test
    public void getUserById_WhenNotExists_ShouldReturn404() throws Exception {
        given(userService.getUserById(999)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        given(userService.saveUser(any(User.class))).willReturn(testUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andDo(print());
    }

    @Test
    public void updateUser_WhenExists_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User("newpass", "test@gmail.com", "John", "Updated", false);
        updatedUser.setUser_id(1);

        given(userService.getUserById(1)).willReturn(Optional.of(testUser));
        given(userService.saveUser(any(User.class))).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_name").value("Updated"))
                .andDo(print());
    }

    @Test
    public void deleteUser_WhenExists_ShouldReturn200() throws Exception {
        given(userService.getUserById(1)).willReturn(Optional.of(testUser));
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void login_WithValidCredentials_ShouldReturnUser() throws Exception {
        given(userService.findByEmailAndPassword("test@gmail.com", "password123"))
                .willReturn(Optional.of(testUser));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andDo(print());
    }

    @Test
    public void login_WithInvalidCredentials_ShouldReturn404() throws Exception {
        given(userService.findByEmailAndPassword("wrong@gmail.com", "wrongpass"))
                .willReturn(Optional.empty());

        User invalidUser = new User("wrongpass", "wrong@gmail.com", "Wrong", "User", false);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
