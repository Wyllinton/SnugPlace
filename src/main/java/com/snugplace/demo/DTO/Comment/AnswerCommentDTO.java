package com.snugplace.demo.DTO.Comment;

import com.snugplace.demo.DTO.User.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record AnswerCommentDTO(
        @NotNull @NotBlank Long id,
        @NotNull @NotBlank Long idComment,
        UserResponseDTO user,
        @NotBlank String answer,
        @Past LocalDate date
) {
}
