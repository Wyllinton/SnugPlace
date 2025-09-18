package DTO.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ChangeUserPasswordDTO(
        @NotNull @NotBlank @Length(min = 8, max = 30) String currentPassword,
        @NotNull @NotBlank @Length(min = 8, max = 30) String newPassword
) {
}
