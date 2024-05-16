package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.user.UserLoginRequestDto;
import com.github.onlinebookstore.dto.user.UserLoginResponseDto;
import com.github.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.exception.RegistrationException;
import com.github.onlinebookstore.mapper.UserMapper;
import com.github.onlinebookstore.model.Role;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.RoleRepository;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.security.JwtUtil;
import com.github.onlinebookstore.service.impl.AuthenticationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void register_withUserRegisterRequestDto_shouldRegisterUserAndReturnUserResponseDto() {
        UserRegisterRequestDto userRegisterRequestDto = createTestUserRegisterDto();
        User user = mapToModel(1L, userRegisterRequestDto);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toModel(any(UserRegisterRequestDto.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(roleRepository.findByName(any(Role.RoleName.class)))
                .thenReturn(Optional.of(new Role()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponseDto expected = mapToDto(user);
        when(userMapper.toDto(any(User.class))).thenReturn(expected);

        UserResponseDto actual = authenticationService.register(userRegisterRequestDto);

        Assertions.assertEquals(expected, actual,
                "The retrieved user response DTO is not the same as expected");
        verify(userRepository).save(any(User.class));

    }

    @Test
    public void register_withInvalidEmail_shouldThrowException() {
        UserRegisterRequestDto userRegisterRequestDto = createTestUserRegisterDto();
        User user = mapToModel(1L, userRegisterRequestDto);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Assertions.assertThrows(RegistrationException.class,
                () -> authenticationService.register(userRegisterRequestDto)
        );
    }

    @Test
    public void register_withInvalidRoleName_shouldThrowException() {
        UserRegisterRequestDto userRegisterRequestDto = createTestUserRegisterDto();
        User user = mapToModel(1L, userRegisterRequestDto);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toModel(any(UserRegisterRequestDto.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(roleRepository.findByName(any(Role.RoleName.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> authenticationService.register(userRegisterRequestDto)
        );
    }

    @Test
    public void login_withUserLoginRequestDto_shouldLoginAndReturnUserLoginResponseDto() {
        UserLoginRequestDto loginDto = createTestUserLoginDto();
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                loginDto.getPassword());
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);
        String token = "token";
        when(jwtUtil.generateToken(anyString())).thenReturn(token);
        UserLoginResponseDto expected = new UserLoginResponseDto(token);

        UserLoginResponseDto actual = authenticationService.login(loginDto);

        Assertions.assertEquals(expected, actual,
                "The retrieved user login response DTO is not the same as expected");
    }

    private UserLoginRequestDto createTestUserLoginDto() {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("email");
        userLoginRequestDto.setPassword("password");
        return userLoginRequestDto;
    }

    private UserResponseDto mapToDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        return userResponseDto;
    }

    private UserRegisterRequestDto createTestUserRegisterDto() {
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto();
        userRegisterRequestDto.setEmail("email@email.com");
        userRegisterRequestDto.setPassword("password");
        userRegisterRequestDto.setFirstName("firstName");
        userRegisterRequestDto.setLastName("lastName");
        userRegisterRequestDto.setShippingAddress("shippingAddress");
        return userRegisterRequestDto;
    }

    private User mapToModel(Long userId, UserRegisterRequestDto registerRequestDto) {
        User user = new User();
        user.setId(userId);
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(registerRequestDto.getPassword());
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setShippingAddress(registerRequestDto.getShippingAddress());
        return user;
    }
}
