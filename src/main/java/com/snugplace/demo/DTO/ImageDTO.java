package com.snugplace.demo.DTO;

import com.snugplace.demo.Model.Accommodation;
import jakarta.validation.constraints.NotNull;

public record ImageDTO(
        @NotNull Long id,
        @NotNull Long accommodationId,
        @NotNull String url,
        boolean isMainImage
) {
}
