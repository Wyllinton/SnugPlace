package DTO.Comment;

import DTO.User.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CommentDTO(
        Long id,
        Long idBooking,
        @NotNull double rating,
        UserResponseDTO user,
        @NotBlank String comment,
        @Past LocalDate date
) {
}