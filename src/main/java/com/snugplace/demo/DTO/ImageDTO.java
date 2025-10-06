package com.snugplace.demo.DTO;

import com.snugplace.demo.Model.Accommodation;
import jakarta.validation.constraints.NotNull;

public record ImageDTO(
        @NotNull String url
) {
}
