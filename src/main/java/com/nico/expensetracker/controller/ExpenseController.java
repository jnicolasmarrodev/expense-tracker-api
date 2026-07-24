package com.nico.expensetracker.controller;

import com.nico.expensetracker.dto.ExpenseRequestDTO;
import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.dto.ExpenseUpdateRequestDTO;
import com.nico.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @Valid @RequestBody ExpenseRequestDTO dto
    ) {
        return expenseService.create(dto);
    }

    @GetMapping("/{id}")
    public ExpenseResponseDTO findById(
            @PathVariable Long id
    ){
        return expenseService.findById(id);
    }

    @GetMapping
    public List<ExpenseResponseDTO> findAll() {
        return expenseService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(
            @PathVariable Long id
    ) {
        expenseService.delete(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDTO updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseUpdateRequestDTO dto
    ) {
        return expenseService.update(id, dto);
    }
}
