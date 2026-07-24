package com.nico.expensetracker.entity;

import com.nico.expensetracker.dto.ExpenseUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Expense(
            BigDecimal amount,
            String description,
            LocalDate expenseDate,
            ExpenseCategory category,
            Currency currency,
            User user
    ) {
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.category = category;
        this.currency = currency;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public void update(
            ExpenseUpdateRequestDTO dto
    ) {

        this.description = dto.description();
        this.amount = dto.amount();
        this.currency = dto.currency();
        this.category = dto.category();
        this.expenseDate = dto.expenseDate();
    }
}
