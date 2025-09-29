package DTO.Booking;

import Model.Enums.BookingStatus;

import java.time.LocalDate;

public record FilteredBookingDTO(
        String titleAccommodation,
        String description,
        Long id,
        String idAccommodation,
        LocalDate dateCheckIn,
        LocalDate dateCheckOut,
        int guestsCount,
        double price

) {
}
