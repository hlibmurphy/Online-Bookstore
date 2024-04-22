package com.github.onlinebookstore.service;

import com.github.onlinebookstore.model.User;

public interface UserService {

    void deleteById(Long id);

    User findByEmail(String email);
}
