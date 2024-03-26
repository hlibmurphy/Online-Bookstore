package com.github.onlinebookstore.services;

import com.github.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.github.onlinebookstore.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegisterRequestDto requestDto);

    void deleteById(Long id);
}
