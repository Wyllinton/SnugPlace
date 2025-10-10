package com.snugplace.demo.DTO.Comment;

import com.snugplace.demo.Model.User;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateCommentDTO(
        @NotNull Long idBooking,
        @NotNull @DecimalMin("0.0") @DecimalMax("5.0") double rating,
        @NotBlank String comment,
        @PastOrPresent LocalDate date
) {}
