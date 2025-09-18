package DTO.User;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ResetPasswordDTO(
        @NotNull String codigo,
        @Length (min = 8) @NotNull String nuevaPassword
) {
}
