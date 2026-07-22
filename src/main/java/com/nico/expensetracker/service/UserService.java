package com.nico.expensetracker.service;

import com.nico.expensetracker.dto.UserRequestDTO;
import com.nico.expensetracker.dto.UserResponseDTO;
import com.nico.expensetracker.entity.User;
import com.nico.expensetracker.exception.UserNotFoundException;
import com.nico.expensetracker.mapper.UserMapper;
import com.nico.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO create(UserRequestDTO dto) {

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
}
