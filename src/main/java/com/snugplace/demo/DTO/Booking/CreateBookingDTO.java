package com.snugplace.demo.DTO.Booking;

import com.snugplace.demo.Model.Enums.BookingStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateBookingDTO(
        @NotNull Long id,
        @NotNull Long idUser,
        @NotNull Long idAccommodation,
        @NotNull @FutureOrPresent LocalDate dateCheckIn,
        @NotNull @Future LocalDate dateCheckOut,
        @Min(1) int guestsCount,
        @NotNull BookingStatus status,
        @Positive double price,
        @Size(max = 500) String notes
) {
}
