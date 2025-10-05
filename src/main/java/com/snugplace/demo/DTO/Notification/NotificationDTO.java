package com.snugplace.demo.DTO.Notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record NotificationDTO(
        @NotNull @NotBlank Long id,
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String message,
        @NotNull @NotBlank @DateTimeFormat LocalDateTime date,
        Boolean read
) {
}
