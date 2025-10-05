package com.snugplace.demo.DTO.Booking;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FilteredBookingsHostDTO(
        @NotBlank @NotNull Long idAccommodation,
        String status,
        @FutureOrPresent LocalDate fechaCheckIn,
        @Future LocalDate fechaCheckOut,
        @PositiveOrZero Integer page,
        @Min(1) Integer size
) {
}