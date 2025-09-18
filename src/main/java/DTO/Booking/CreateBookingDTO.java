package DTO.Booking;

import Model.Enums.BookingStatus;

import java.time.LocalDate;

public record CreateBookingDTO(
        String id,
        String idUser,
        String idAccommodation,
        LocalDate dateCheckIn,
        LocalDate dateCheckOut,
        int guestsCount,
        BookingStatus status,
        double price
) {
}
