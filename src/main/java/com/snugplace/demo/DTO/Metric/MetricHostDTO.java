package com.snugplace.demo.DTO.Metric;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetricHostDTO(
        @NotNull @NotBlank Long idAccommodation,
        @NotNull @NotBlank String title,
        @NotNull @NotBlank int bookingsConfirmed,
        @NotNull @NotBlank int bookingsCanceled,
        @NotNull @NotBlank int bookingsCompleted,
        @NotNull @NotBlank int comments,
        @NotNull @NotBlank int totalBookings,
        @NotNull @NotBlank double averageRating
) {
}
