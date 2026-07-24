package com.nico.expensetracker.service;

import com.nico.expensetracker.dto.ExpenseRequestDTO;
import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.entity.Currency;
import com.nico.expensetracker.entity.Expense;
import com.nico.expensetracker.entity.ExpenseCategory;
import com.nico.expensetracker.entity.User;
import com.nico.expensetracker.exception.ExpenseNotFoundException;
import com.nico.expensetracker.exception.UserNotFoundException;
import com.nico.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {


    @Test
    void shouldCreateExpenseSuccessfully() {

        // Arrange
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = mock(UserService.class);


        ExpenseService expenseService =
                new ExpenseService(
                        expenseRepository,
                        userService
                );


        User user = new User(
                "Nicolas",
                "nicolas@test.com",
                "123456"
        );

        // Simulamos que el usuario ya existe en la BD
        ReflectionTestUtils.setField(user, "id", 1L);


        ExpenseRequestDTO request = new ExpenseRequestDTO(
                "Almuerzo",
                new BigDecimal("25000"),
                Currency.COP,
                ExpenseCategory.FOOD,
                LocalDate.now(),
                1L
        );


        when(userService.findByIdOrThrow(1L))
                .thenReturn(user);


        when(expenseRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // Act
        ExpenseResponseDTO response =
                expenseService.create(request);


        // Assert
        assertThat(response)
                .isNotNull();

        assertThat(response.description())
                .isEqualTo("Almuerzo");

        assertThat(response.amount())
                .isEqualByComparingTo("25000");

        assertThat(response.currency())
                .isEqualTo(Currency.COP);

        assertThat(response.category())
                .isEqualTo(ExpenseCategory.FOOD);

        assertThat(response.userId())
                .isEqualTo(1L);


        verify(userService)
                .findByIdOrThrow(1L);

        verify(expenseRepository)
                .save(any());
    }

    @Test
    void shouldFindExpenseByIdSuccessfully() {

        // Arrange
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = mock(UserService.class);

        ExpenseService expenseService =
                new ExpenseService(
                        expenseRepository,
                        userService
                );


        User user = new User(
                "Nicolas",
                "nicolas@test.com",
                "123456"
        );

        ReflectionTestUtils.setField(user, "id", 1L);


        Expense expense = new Expense(
                new BigDecimal("25000"),
                "Almuerzo",
                LocalDate.now(),
                ExpenseCategory.FOOD,
                Currency.COP,
                user
        );

        ReflectionTestUtils.setField(expense, "id", 10L);


        when(expenseRepository.findById(10L))
                .thenReturn(java.util.Optional.of(expense));


        // Act
        ExpenseResponseDTO response =
                expenseService.findById(10L);


        // Assert
        assertThat(response)
                .isNotNull();

        assertThat(response.id())
                .isEqualTo(10L);

        assertThat(response.description())
                .isEqualTo("Almuerzo");

        assertThat(response.userId())
                .isEqualTo(1L);


        verify(expenseRepository)
                .findById(10L);
    }

    @Test
    void shouldThrowExceptionWhenExpenseDoesNotExist() {

        // Arrange
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = mock(UserService.class);

        ExpenseService expenseService =
                new ExpenseService(
                        expenseRepository,
                        userService
                );


        when(expenseRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());


        // Act + Assert
        assertThatThrownBy(() ->
                expenseService.findById(999L)
        )
                .isInstanceOf(ExpenseNotFoundException.class)
                .hasMessage("Expense not found with id: 999");


        verify(expenseRepository)
                .findById(999L);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = mock(UserService.class);


        ExpenseService expenseService =
                new ExpenseService(
                        expenseRepository,
                        userService
                );


        ExpenseRequestDTO request = new ExpenseRequestDTO(
                "Almuerzo",
                new BigDecimal("25000"),
                Currency.COP,
                ExpenseCategory.FOOD,
                LocalDate.now(),
                999L
        );


        when(userService.findByIdOrThrow(999L))
                .thenThrow(new UserNotFoundException(999L));


        // Act + Assert
        assertThatThrownBy(() ->
                expenseService.create(request)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: 999");


        verify(userService)
                .findByIdOrThrow(999L);


        verifyNoInteractions(expenseRepository);
    }
}
