package com.nico.expensetracker.dto;

public record UserRequestDTO(
        String name,
        String email,
        String password
) {
}
