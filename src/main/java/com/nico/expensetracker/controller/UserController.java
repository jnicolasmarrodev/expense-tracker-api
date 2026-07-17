package com.nico.expensetracker.controller;

import com.nico.expensetracker.dto.UserRequestDTO;
import com.nico.expensetracker.dto.UserResponseDTO;
import com.nico.expensetracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(
            @RequestBody UserRequestDTO dto
    ) {
        return userService.create(dto);
    }
}
