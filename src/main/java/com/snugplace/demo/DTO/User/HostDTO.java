package com.snugplace.demo.DTO.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record HostDTO(
        @NotNull Long id,
        @NotNull String name,
        @Email @NotNull String email
) {
}
