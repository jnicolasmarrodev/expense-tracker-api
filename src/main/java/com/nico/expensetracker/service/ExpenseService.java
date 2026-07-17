package com.nico.expensetracker.service;

import com.nico.expensetracker.dto.ExpenseRequestDTO;
import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.entity.Expense;
import com.nico.expensetracker.entity.User;
import com.nico.expensetracker.mapper.ExpenseMapper;
import com.nico.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserService userService
    ) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    public ExpenseResponseDTO create(ExpenseRequestDTO dto) {

        User user = userService.findEntityById(dto.userId());

        Expense expense = ExpenseMapper.toEntity(dto, user);

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(savedExpense);
    }
}
