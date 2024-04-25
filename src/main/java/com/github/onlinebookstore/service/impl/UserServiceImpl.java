package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.service.UserService;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("Cannot find user with email " + email));

        return user;
    }
}
