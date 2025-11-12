package com.snugplace.demo.DTO.Accommodation;

import com.snugplace.demo.Model.Image;
import jakarta.validation.constraints.*;


public record AccommodationCardDTO (
        @NotNull Long id,
        @NotBlank @Size(max = 100) String title,
        @NotBlank String city,
        @Positive double priceDay,
        @DecimalMin("0.0") @DecimalMax("5.0") double averageRating,
        @NotNull Image mainImage
) {}
