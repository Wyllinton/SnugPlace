package DTO.Notification;

import java.time.LocalDate;

public record NotificationDTO(
        String id,
        String title,
        String message,
        LocalDate date,
        Boolean read
) {
}
