package com.example.internetforum.models.dto;

import com.example.internetforum.models.XSSValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank
    @XSSValid
    private String username;
    @NotBlank
    @Size(max = 30)
    @XSSValid
    private String password;
}
