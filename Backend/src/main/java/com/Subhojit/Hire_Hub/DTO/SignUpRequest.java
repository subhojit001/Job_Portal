package com.Subhojit.Hire_Hub.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Name should not be empty")
    private String name;

    @Email(message = "invalid email")
    @NotBlank
    private String email;

    @Size(min = 5, message = "at least 5 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;
}
