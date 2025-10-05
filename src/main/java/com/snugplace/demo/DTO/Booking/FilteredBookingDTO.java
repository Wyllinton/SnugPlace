package com.snugplace.demo.DTO.Booking;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FilteredBookingDTO(
        @NotBlank String titleAccommodation,
        @NotBlank String description,
        @NotBlank @NotNull Long id,
        @NotBlank @NotNull Long idAccommodation,
        @FutureOrPresent LocalDate dateCheckIn,
        @Future LocalDate dateCheckOut,
        @Min(1) int guestsCount,
        @PositiveOrZero double price,
        @PositiveOrZero Integer page
) {
}
