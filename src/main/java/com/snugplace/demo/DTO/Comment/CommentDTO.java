package com.snugplace.demo.DTO.Comment;

import com.snugplace.demo.DTO.User.UserResponseDTO;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CommentDTO(
        @NotNull Long id,
        @NotNull Long idBooking,
        @NotNull @DecimalMin("0.0") @DecimalMax("5.0") double rating,
        @NotNull UserResponseDTO user,
        @NotBlank String comment,
        @NotNull @Past LocalDate date
) {}
