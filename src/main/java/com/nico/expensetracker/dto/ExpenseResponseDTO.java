package com.nico.expensetracker.dto;

import com.nico.expensetracker.entity.Currency;
import com.nico.expensetracker.entity.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseResponseDTO(
        Long id,
        String description,
        BigDecimal amount,
        Currency currency,
        ExpenseCategory category,
        LocalDate expenseDate,
        LocalDateTime createdAt,
        Long userId
) {
}
