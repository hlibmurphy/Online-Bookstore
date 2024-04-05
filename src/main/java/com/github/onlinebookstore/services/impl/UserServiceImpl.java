package com.github.onlinebookstore.services.impl;

import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.mapper.UserMapper;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repositories.UserRepository;
import com.github.onlinebookstore.services.UserService;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("Cannot find user with email " + email));

        return userMapper.toDto(user);
    }
}
