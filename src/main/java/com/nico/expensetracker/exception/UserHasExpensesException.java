package com.nico.expensetracker.exception;

public class UserHasExpensesException extends RuntimeException {

    public UserHasExpensesException(Long id) {
        super("User cannot be deleted because it has associated expenses. User id: " + id);
    }
}
