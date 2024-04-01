package com.github.onlinebookstore.services.impl;

import com.github.onlinebookstore.repositories.UserRepository;
import com.github.onlinebookstore.services.UserService;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDetails findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("Cannot find user with email " + email));
    }
}
