package com.snugplace.demo.DTO.Metric;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record MetricAccommodationDTO(
        Long idAccommodation,
        @NotNull String title,
        @Past Date startDate,
        @Past Date endDate,
        int countBookings,
        int confirmedBookings,
        int cancelledBookings,
        int completedBookings,
        double averageRating,
        double totalIncomes
) {
}
