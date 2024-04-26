package com.example.internetforum.models.dto;

import com.example.internetforum.models.XSSValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeUserRoleRequest {
    @NotNull
    @Min(0)
    Integer id;
    @NotBlank
    @Pattern(regexp = "^(administrator|moderator|forum_member)")
    @XSSValid
    String role;
}
