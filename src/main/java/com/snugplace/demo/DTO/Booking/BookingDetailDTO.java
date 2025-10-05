package com.snugplace.demo.DTO.Booking;

import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.User.UserResponseDTO;
import com.snugplace.demo.Model.Enums.BookingStatus;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record BookingDetailDTO(
        @NotNull @NotBlank Long id,
        @NotNull Long idAccommodation,
        @NotNull UserResponseDTO user,
        @FutureOrPresent LocalDate dateCheckIn,
        @Future LocalDate dateCheckOut,
        @Min(1) int guestsCount,
        @NotNull BookingStatus status,
        @PositiveOrZero double price,
        @NotNull LocalDate createDate,
        List<CommentDTO> comments
) {
}
