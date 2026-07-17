package com.nico.expensetracker.controller;

import com.nico.expensetracker.dto.ExpenseRequestDTO;
import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponseDTO createExpense(
            @RequestBody ExpenseRequestDTO dto
    ) {
        return expenseService.create(dto);
    }
}
