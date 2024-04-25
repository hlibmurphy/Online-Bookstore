package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.user.UserLoginRequestDto;
import com.github.onlinebookstore.dto.user.UserLoginResponseDto;
import com.github.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.github.onlinebookstore.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto register(UserRegisterRequestDto requestDto);

    UserLoginResponseDto login(UserLoginRequestDto requestDto);
}
