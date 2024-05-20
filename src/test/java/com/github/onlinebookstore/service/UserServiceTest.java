package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.user.UserResponseDto;
import com.github.onlinebookstore.mapper.UserMapper;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void deleteById_withValidUserId_shouldDeleteUser() {
        User user = createTestUser(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserResponseDto expected = mapToDto(user);
        when(userMapper.toDto(any(User.class))).thenReturn(expected);

        UserResponseDto actual = userService.deleteById(1L);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match expected DTO");
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void findByEmail_withValidEmail_shouldReturnUser() {
        User user = createTestUser(1L, "test@email.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserResponseDto expected = mapToDto(user);
        when(userMapper.toDto(any(User.class))).thenReturn(expected);

        UserResponseDto actual = userService.findByEmail(user.getEmail());
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match expected DTO");
    }

    @Test
    public void findByEmail_withInvalidEmail_shouldReturnUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.findByEmail(
                "test@email.com"));
    }

    private UserResponseDto mapToDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }

    private User createTestUser(Long userId, String email) {
        User user = new User();
        user.setId(userId);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail(email);
        return user;
    }

    private User createTestUser(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        return user;
    }
}
