package Service;

import DTO.Notification.MarkReadNotificationDTO;
import DTO.Notification.NotificationDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotifications() throws Exception;

    int getNumberNotifications() throws Exception;

    void markAsRead(MarkReadNotificationDTO markReadNotificationDTO) throws Exception;
}
