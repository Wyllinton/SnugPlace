package Model;

import Model.Enums.TypeNotification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class Notification {
    private String id;
    private User receiver;
    private String title;
    private String message;
    private TypeNotification typeNotification;
    private LocalDateTime date;
}
