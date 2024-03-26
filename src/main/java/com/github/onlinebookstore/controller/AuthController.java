package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto register(@Validated @RequestBody UserRegisterRequestDto requestDto) {
        return userService.register(requestDto);
    }
}
