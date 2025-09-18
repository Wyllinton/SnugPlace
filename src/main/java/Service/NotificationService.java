package Service;

import DTO.MarkReadNotificationDTO;
import DTO.NotificationDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotifications() throws Exception;

    int getNumberNotifications() throws Exception;

    void markAsRead(MarkReadNotificationDTO markReadNotificationDTO) throws Exception;
}
