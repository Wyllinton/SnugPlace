package com.snugplace.demo.DTO.Booking;

import com.snugplace.demo.Model.Enums.BookingStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateBookingDTO(
        @NotNull Long idAccommodation,
        @NotNull @FutureOrPresent LocalDate dateCheckIn,
        @NotNull @Future LocalDate dateCheckOut,
        @Min(1) int guestsCount,
        @Size(max = 500) String notes
) {
}
