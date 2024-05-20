package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.mapper.UserMapper;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public UserResponseDto deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
        userRepository.deleteById(id);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with email " + email));

        return userMapper.toDto(user);
    }
}
