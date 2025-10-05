package com.snugplace.demo.DTO.Notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarkReadNotificationDTO(
        @NotBlank @NotNull Long id,
        String message
) {
}
