package com.snugplace.demo.DTO.Metric;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Date;

public record MetricAccommodationDTO(
        Long idAccommodation,
        @NotNull String title,
        @Past LocalDate startDate,
        @Past LocalDate endDate,
        int countBookings,
        int confirmedBookings,
        int cancelledBookings,
        int completedBookings,
        double averageRating,
        double totalIncomes
) {
}
