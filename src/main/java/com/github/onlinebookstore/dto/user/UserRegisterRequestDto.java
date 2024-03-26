package com.github.onlinebookstore.dto.user;

import com.github.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "repeatPassword")
public class UserRegisterRequestDto {
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    @Length(min = 8, max = 8)
    private String password;
    @NotEmpty
    private String repeatPassword;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String shippingAddress;
}
