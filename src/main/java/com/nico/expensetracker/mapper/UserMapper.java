package com.nico.expensetracker.mapper;

import com.nico.expensetracker.dto.UserRequestDTO;
import com.nico.expensetracker.dto.UserResponseDTO;
import com.nico.expensetracker.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequestDTO dto) {

        return new User(
                dto.name(),
                dto.email(),
                dto.password()
        );
    }

    public static UserResponseDTO toResponse(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
