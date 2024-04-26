package com.example.internetforum.models.dto;

import com.example.internetforum.models.XSSValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank
    @XSSValid
    private String username;
    @NotBlank
    @Size(max = 30)
    @XSSValid
    private String password;
    @NotBlank
    @XSSValid
    private String firstName;
    @NotBlank
    @XSSValid
    private String lastName;
    @NotBlank
    @Email
    @XSSValid
    private String email;
}
