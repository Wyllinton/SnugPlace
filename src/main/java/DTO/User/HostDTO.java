package DTO.User;

import jakarta.validation.constraints.NotNull;

public record HostDTO(
        @NotNull Long id,
        @NotNull String name,
        @NotNull String email
) {
}
