package com.example.internetforum.models.dto;

import com.example.internetforum.models.entities.PermissionEntity;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class VerificationUserResponse {
    @NotNull
    @Min(0)
    private Integer id;
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private List<String> permissions;
    @NotBlank
    @Size(max = 6)
    private String token;
    @NotBlank
    @Pattern(regexp = "^(administrator|moderator|forum_member)")
    String role;
}
