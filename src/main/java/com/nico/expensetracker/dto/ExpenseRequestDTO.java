package com.nico.expensetracker.dto;

import com.nico.expensetracker.entity.Currency;
import com.nico.expensetracker.entity.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequestDTO(
        String description,
        BigDecimal amount,
        Currency currency,
        ExpenseCategory category,
        LocalDate expenseDate,
        Long userId
) {
}
