package com.nico.expensetracker.controller;

import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.entity.Currency;
import com.nico.expensetracker.entity.ExpenseCategory;
import com.nico.expensetracker.exception.ExpenseNotFoundException;
import com.nico.expensetracker.exception.UserNotFoundException;
import com.nico.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;


    @Test
    void shouldCreateExpenseSuccessfully() throws Exception {

        ExpenseResponseDTO response = new ExpenseResponseDTO(
                1L,
                "Almuerzo",
                new BigDecimal("25000"),
                Currency.COP,
                ExpenseCategory.FOOD,
                LocalDate.now(),
                LocalDateTime.now(),
                1L
        );


        when(expenseService.create(any()))
                .thenReturn(response);


        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Almuerzo",
                                  "amount": 25000,
                                  "currency": "COP",
                                  "category": "FOOD",
                                  "expenseDate": "2026-07-22",
                                  "userId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Almuerzo"))
                .andExpect(jsonPath("$.currency").value("COP"))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.userId").value(1));
    }


    @Test
    void shouldReturnBadRequestWhenExpenseDataIsInvalid() throws Exception {

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Almuerzo",
                                  "amount": -100,
                                  "currency": null,
                                  "category": null,
                                  "expenseDate": null,
                                  "userId": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        when(expenseService.create(any()))
                .thenThrow(new UserNotFoundException(999L));


        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Almuerzo",
                                  "amount": 25000,
                                  "currency": "COP",
                                  "category": "FOOD",
                                  "expenseDate": "2026-07-22",
                                  "userId": 999
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("User not found with id: 999"));
    }

    @Test
    void shouldReturnExpenseByIdSuccessfully() throws Exception {

        ExpenseResponseDTO response = new ExpenseResponseDTO(
                1L,
                "Almuerzo",
                new BigDecimal("25000"),
                Currency.COP,
                ExpenseCategory.FOOD,
                LocalDate.now(),
                LocalDateTime.now(),
                1L
        );


        when(expenseService.findById(1L))
                .thenReturn(response);


        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Almuerzo"))
                .andExpect(jsonPath("$.amount").value(25000))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldReturnNotFoundWhenExpenseDoesNotExist() throws Exception {

        when(expenseService.findById(999L))
                .thenThrow(new ExpenseNotFoundException(999L));


        mockMvc.perform(get("/api/expenses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Expense not found with id: 999"));
    }
}
