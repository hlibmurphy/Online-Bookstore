package com.github.onlinebookstore.dto.user;

import com.github.onlinebookstore.model.Role;
import java.util.Set;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
    private Set<Role> roles;
}
