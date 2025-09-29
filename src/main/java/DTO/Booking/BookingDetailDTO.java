package DTO.Booking;

import DTO.Accommodation.AccommodationDTO;
import DTO.Comment.CommentDTO;
import DTO.User.UserResponseDTO;
import Model.Enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public record BookingDetailDTO(
        Long id,
        AccommodationDTO accommodation,
        UserResponseDTO user,
        LocalDate dateCheckIn,
        LocalDate dateCheckOut,
        int guestsCount,
        BookingStatus status,
        double price,
        LocalDate createDate,
        List<CommentDTO> comments
) {
}
