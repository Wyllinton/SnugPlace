package DTO.Comment;

import DTO.User.UserResponseDTO;

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
