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
    public UserResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with this email already exists: "
                    + requestDto.getEmail());
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());

        Role userRole = roleRepository.findByName(Role.RoleName.USER);
        userRole.setName(Role.RoleName.USER);
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
