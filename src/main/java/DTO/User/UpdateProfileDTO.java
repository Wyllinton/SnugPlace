package DTO.User;


import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDTO(
        @NotBlank String id,
        @NotBlank String name,
        @NotBlank String phoneNumber,
        String photoURL,
        @NotBlank String description
)
{
}
