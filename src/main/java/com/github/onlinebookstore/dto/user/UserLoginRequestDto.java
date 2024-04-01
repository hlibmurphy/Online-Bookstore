package com.github.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @Email
    private String email;
    private String password;
}
