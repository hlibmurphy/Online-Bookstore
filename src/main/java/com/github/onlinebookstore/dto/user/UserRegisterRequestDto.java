package com.github.onlinebookstore.dto.user;

import com.github.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "repeatPassword")
public class UserRegisterRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 8)
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
