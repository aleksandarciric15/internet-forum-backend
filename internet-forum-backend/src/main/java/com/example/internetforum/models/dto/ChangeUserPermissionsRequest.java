package com.example.internetforum.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ChangeUserPermissionsRequest {
    @NotNull
    @Min(0)
    Integer userId;
    @NotNull
    List<String> permissions;
}
