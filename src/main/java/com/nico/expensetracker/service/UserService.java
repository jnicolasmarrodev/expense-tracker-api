package com.nico.expensetracker.service;

import com.nico.expensetracker.dto.UserRequestDTO;
import com.nico.expensetracker.dto.UserResponseDTO;
import com.nico.expensetracker.entity.User;
import com.nico.expensetracker.exception.EmailAlreadyExistsException;
import com.nico.expensetracker.exception.UserHasExpensesException;
import com.nico.expensetracker.exception.UserNotFoundException;
import com.nico.expensetracker.mapper.UserMapper;
import com.nico.expensetracker.repository.ExpenseRepository;
import com.nico.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ExpenseRepository expenseRepository;

    public UserService(UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    public UserResponseDTO create(UserRequestDTO dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = UserMapper.toEntity(dto);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    public User findByIdOrThrow(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id));
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return UserMapper.toResponse(user);
    }

    public UserResponseDTO update(
            Long id,
            UserRequestDTO dto
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));


        user.update(
                dto.name(),
                dto.email(),
                dto.password()
        );


        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (expenseRepository.existsByUserId(id)) {
            throw new UserHasExpensesException(id);
        }

        userRepository.delete(user);
    }
}
