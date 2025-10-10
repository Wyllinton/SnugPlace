package com.snugplace.demo.DTO;

import jakarta.validation.constraints.*;

public record AuthResponseDTO(
        @NotBlank(message = "Token is required")
        String token
) {}
