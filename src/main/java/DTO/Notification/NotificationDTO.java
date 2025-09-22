package DTO.Notification;

import Model.Enums.TypeNotification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NotificationDTO(
        Long id,
        @NotNull TypeNotification typeNotification,
        @NotBlank String message,
        LocalDate createdAt
) {
}
