package com.nico.expensetracker.service;

import com.nico.expensetracker.entity.Expense;
import com.nico.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }
}
