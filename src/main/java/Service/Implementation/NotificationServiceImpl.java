package Service.Implementation;

import DTO.Notification.MarkReadNotificationDTO;
import DTO.Notification.NotificationDTO;
import Service.NotificationService;

import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public List<NotificationDTO> getNotifications() throws Exception {
        return List.of();
    }

    @Override
    public int getNumberNotifications() throws Exception {
        return 0;
    }

    @Override
    public void markAsRead(MarkReadNotificationDTO markReadNotificationDTO) throws Exception {

    }
}
