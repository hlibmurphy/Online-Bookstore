package com.github.onlinebookstore.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    void deleteById(Long id);

    UserDetails findByEmail(String email);
}
