package com.snugplace.demo.DTO.Metric;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MetricRequestDTO(
        @NotNull LocalDate firstDate,
        @NotNull LocalDate lasDate
) {
}
