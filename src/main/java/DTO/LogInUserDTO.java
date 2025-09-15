package DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LogInUserDTO (
        @Email @NotNull @Length(max = 40) String email,
    String password
)
{
}
