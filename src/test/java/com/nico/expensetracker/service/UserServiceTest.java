package com.nico.expensetracker.service;

import com.nico.expensetracker.dto.UserRequestDTO;
import com.nico.expensetracker.dto.UserResponseDTO;
import com.nico.expensetracker.entity.User;
import com.nico.expensetracker.exception.EmailAlreadyExistsException;
import com.nico.expensetracker.exception.UserNotFoundException;
import com.nico.expensetracker.repository.ExpenseRepository;
import com.nico.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void shouldCreateUserSuccessfully() {

        UserRepository userRepository = mock(UserRepository.class);
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = new UserService(userRepository, expenseRepository);


        UserRequestDTO request = new UserRequestDTO(
                "Nicolas",
                "nicolas@test.com",
                "123456"
        );


        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);


        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        UserResponseDTO response = userService.create(request);


        assertThat(response.email())
                .isEqualTo("nicolas@test.com");


        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserRepository userRepository = mock(UserRepository.class);
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = new UserService(userRepository, expenseRepository);


        UserRequestDTO request = new UserRequestDTO(
                "Nicolas",
                "nicolas@test.com",
                "123456"
        );


        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);


        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(EmailAlreadyExistsException.class);


        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void shouldReturnUserWhenUserExists() {

        UserRepository userRepository = mock(UserRepository.class);
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = new UserService(userRepository, expenseRepository);


        User user = new User(
                "Nicolas",
                "nicolas@test.com",
                "123456"
        );


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));


        User result = userService.findByIdOrThrow(1L);


        assertThat(result.getEmail())
                .isEqualTo("nicolas@test.com");
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UserRepository userRepository = mock(UserRepository.class);
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);

        UserService userService = new UserService(userRepository, expenseRepository);


        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() ->
                userService.findByIdOrThrow(999L))
                .isInstanceOf(UserNotFoundException.class);
    }
}