package com.snugplace.demo.DTO.Booking;

import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.User.UserResponseDTO;
import com.snugplace.demo.Model.Enums.BookingStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record BookingDTO(
        @NotNull Long id,
        @NotNull Long idAccommodation,
        @NotNull UserResponseDTO user,
        @FutureOrPresent LocalDate dateCheckIn,
        @Future LocalDate dateCheckOut,
        @Min(1) int guestsCount,
        @NotNull BookingStatus status,
        @PositiveOrZero double price,
        List<CommentDTO> comments,
        // NUEVO CAMPO: para identificar el contexto de la reserva
        @NotNull Boolean isMyOwnBooking // true = reserva que YO hice, false = reserva en mi propiedad
) {
}