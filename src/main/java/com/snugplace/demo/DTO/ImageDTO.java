package com.snugplace.demo.DTO;

import com.snugplace.demo.Model.Accommodation;
import jakarta.validation.constraints.NotNull;

public record ImageDTO(
        String url,
        String cloudinaryId,
        boolean isMainImage
) {
}
