package com.nico.expensetracker.mapper;

import com.nico.expensetracker.dto.ExpenseRequestDTO;
import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.entity.Expense;
import com.nico.expensetracker.entity.User;

public final class ExpenseMapper {

    private ExpenseMapper() {
    }

    public static Expense toEntity(
            ExpenseRequestDTO dto,
            User user
    ) {
        return new Expense(
                dto.amount(),
                dto.description(),
                dto.expenseDate(),
                dto.category(),
                dto.currency(),
                user
        );
    }

    public static ExpenseResponseDTO toResponse(
            Expense expense
    ) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getCategory(),
                expense.getExpenseDate(),
                expense.getCreatedAt(),
                expense.getUser().getId()
        );
    }
}
