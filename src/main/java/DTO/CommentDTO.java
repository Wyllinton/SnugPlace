package DTO;

import DTO.User.UserResponseDTO;
import Model.User;

import java.time.LocalDate;

public record CommentDTO(
    String id,
    String idBooking,
    UserResponseDTO user,
    double rating,
    String comment,
    LocalDate date
) {
}
