package DTO.Comment;

import DTO.User.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record AnswerCommentDTO(
        Long idAnswer,
        Long idComment,
        UserResponseDTO user,
        @NotBlank String answer,
        @Past LocalDate date
) {
}
