package com.github.onlinebookstore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponseDto {
    private String token;
}
