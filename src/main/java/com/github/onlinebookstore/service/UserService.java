package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto deleteById(Long id);

    UserResponseDto findByEmail(String email);
}
