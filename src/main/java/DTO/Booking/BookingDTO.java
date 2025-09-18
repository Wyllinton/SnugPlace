package DTO.Booking;

import Model.Enums.BookingStatus;

import java.time.LocalDate;

public record BookingDTO(
        String id,
        String idAccommodation,
        String titleAccommodation,
        LocalDate dateCheckIn,
        LocalDate dateCheckOut,
        int guestsCount,
        BookingStatus status,
        double price
) {
}
