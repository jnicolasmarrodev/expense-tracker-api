package com.nico.expensetracker.controller;

import com.nico.expensetracker.dto.UserRequestDTO;
import com.nico.expensetracker.dto.UserResponseDTO;
import com.nico.expensetracker.exception.EmailAlreadyExistsException;
import com.nico.expensetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    void shouldReturnAllUsers() throws Exception {

        UserResponseDTO user = new UserResponseDTO(
                1L,
                "Nicolas",
                "nicolas@test.com",
                LocalDateTime.now()
        );


        when(userService.findAll())
                .thenReturn(List.of(user));


        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nicolas"))
                .andExpect(jsonPath("$[0].email").value("nicolas@test.com"));
    }


    @Test
    void shouldReturnUserById() throws Exception {

        UserResponseDTO user = new UserResponseDTO(
                1L,
                "Nicolas",
                "nicolas@test.com",
                LocalDateTime.now()
        );


        when(userService.findById(1L))
                .thenReturn(user);


        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nicolas"))
                .andExpect(jsonPath("$.email").value("nicolas@test.com"));
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        UserRequestDTO request = new UserRequestDTO(
                "Nicolas",
                "nicolas@test.com",
                "123456"
        );


        UserResponseDTO response = new UserResponseDTO(
                1L,
                "Nicolas",
                "nicolas@test.com",
                LocalDateTime.now()
        );


        when(userService.create(any(UserRequestDTO.class)))
                .thenReturn(response);


        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Nicolas",
                              "email": "nicolas@test.com",
                              "password": "123456"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nicolas"))
                .andExpect(jsonPath("$.email").value("nicolas@test.com"));


        verify(userService)
                .create(any(UserRequestDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenUserDataIsInvalid() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "",
                              "email": "correo-invalido",
                              "password": "123"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Name is required"))
                .andExpect(jsonPath("$.errors.email")
                        .value("Email is invalid"))
                .andExpect(jsonPath("$.errors.password")
                        .value("Password must have at least 6 characters"));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

        when(userService.create(any(UserRequestDTO.class)))
                .thenThrow(new EmailAlreadyExistsException("nicolas@test.com"));


        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Nicolas",
                              "email": "nicolas@test.com",
                              "password": "123456"
                            }
                            """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Email already exists: nicolas@test.com"));
    }
}