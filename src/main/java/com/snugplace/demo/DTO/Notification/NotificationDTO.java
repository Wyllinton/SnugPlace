package com.snugplace.demo.DTO.Notification;

import com.snugplace.demo.Model.Enums.TypeNotification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record NotificationDTO(
        @NotNull @NotBlank Long idReceiver,
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String message,
        Boolean read,
        TypeNotification type
) {
}
