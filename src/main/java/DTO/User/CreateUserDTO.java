package DTO.User;

import Model.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record CreateUserDTO (
        @NotNull @Length(max = 100) String name,
        @Email @NotNull @Length(max = 40) String email,
        @NotBlank @Length(min = 8, max = 30) String password,
        @NotNull @Length(max = 10) String phoneNumber,
        @NotNull  @Past LocalDate birhtDate,
        Role role,
        @Length(max = 300) String description,
        @Length(max = 300) String profilePhoto)
{

}
