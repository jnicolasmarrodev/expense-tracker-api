package com.nico.expensetracker.repository;

import com.nico.expensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    boolean existsByUserId(Long userId);
}
