package com.snugplace.demo.DTO.User;


import com.snugplace.demo.Model.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserDTO(
        @NotBlank Long id,
        @NotNull String name,
        @Email @NotNull @Length(max = 40) String email,
        @NotNull String photoUrl,
        Role role
)
{
}
