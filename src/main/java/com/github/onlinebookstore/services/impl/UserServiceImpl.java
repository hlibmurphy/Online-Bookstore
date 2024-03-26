package com.github.onlinebookstore.services.impl;

import com.github.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.exception.RegistrationException;
import com.github.onlinebookstore.mapper.UserMapper;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repositories.UserRepository;
import com.github.onlinebookstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with this email already exists: "
                    + requestDto.getEmail());
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
