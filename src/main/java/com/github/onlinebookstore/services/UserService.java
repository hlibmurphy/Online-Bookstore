package com.github.onlinebookstore.services;

import com.github.onlinebookstore.dto.user.UserResponseDto;

public interface UserService {

    void deleteById(Long id);

    UserResponseDto findByEmail(String email);
}
