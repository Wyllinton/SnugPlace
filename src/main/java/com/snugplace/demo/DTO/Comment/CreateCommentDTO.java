package com.snugplace.demo.DTO.Comment;

import com.snugplace.demo.DTO.User.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateCommentDTO(
        @NotNull @NotBlank Long id,
        @NotNull @NotBlank Long idBooking,
        @NotNull UserResponseDTO user,
        @NotNull double rating,
        @NotNull @NotBlank String comment,
        @Past LocalDate date
) {
}
