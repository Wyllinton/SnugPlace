package com.snugplace.demo.DTO.Metric;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetricDTO(
        @NotNull @NotBlank Long id,
        @NotNull @NotBlank Long idAccommodation,
        @NotNull @NotBlank int totalBookings,
        @NotNull @NotBlank double averageRating,
        @NotNull @NotBlank double totalMount
        )
{
}
