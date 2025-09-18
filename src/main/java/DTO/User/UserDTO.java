package DTO.User;


import Model.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserDTO(
        String id,
        @NotNull String name,
        @Email @NotNull @Length(max = 40) String email,
        String photoUrl,
        Role role
)
{
}
