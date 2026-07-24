package com.nico.expensetracker.dto;

import com.nico.expensetracker.entity.Currency;
import com.nico.expensetracker.entity.ExpenseCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseUpdateRequestDTO(

        String description,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Currency is required")
        Currency currency,

        @NotNull(message = "Expense Category is required")
        ExpenseCategory category,

        @NotNull(message = "Expense Date is required")
        LocalDate expenseDate

) {
}
