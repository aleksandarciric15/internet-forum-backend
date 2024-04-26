package com.example.internetforum.models.dto;

import com.example.internetforum.models.XSSValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditCommentRequest {
    @NotNull
    @Min(0)
    Integer id;
    @NotBlank
    @Size(max = 2000)
    @XSSValid
    String comment;
}