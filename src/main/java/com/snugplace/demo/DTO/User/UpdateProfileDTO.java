package com.snugplace.demo.DTO.User;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProfileDTO(
        @NotBlank String name,
        @NotBlank String phoneNumber,
        @NotNull String photoURL,
        @NotBlank String description
)
{
}
