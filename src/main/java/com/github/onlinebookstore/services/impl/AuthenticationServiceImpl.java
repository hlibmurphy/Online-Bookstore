package com.github.onlinebookstore.services.impl;

import com.github.onlinebookstore.dto.user.UserLoginRequestDto;
import com.github.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.exception.RegistrationException;
import com.github.onlinebookstore.mapper.UserMapper;
import com.github.onlinebookstore.model.Role;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repositories.RoleRepository;
import com.github.onlinebookstore.repositories.UserRepository;
import com.github.onlinebookstore.services.AuthenticationService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with this email already exists: "
                    + requestDto.getEmail());
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER).orElseThrow(
                () -> new RuntimeException("Cannot find a role " + Role.RoleName.ROLE_USER));
        userRole.setName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public boolean login(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        if (user.isEmpty()) {
            return false;
        }

        return requestDto.getPassword().equals(user.get().getPassword());
    }
}