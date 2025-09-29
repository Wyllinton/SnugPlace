package Service.Implementation;

import DTO.Notification.MarkReadNotificationDTO;
import DTO.Notification.NotificationDTO;
import Model.Notification;
import Service.NotificationService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationServiceImpl implements NotificationService {

    private final Map<String, Notification> notificationStore = new ConcurrentHashMap<>();


    @Override
    public List<NotificationDTO> getNotifications() throws Exception {
        return List.of();
    }

    @Override
    public int getNumberNotifications() throws Exception {
        return (int) notificationStore.size();
    }

    @Override
    public void markAsRead(MarkReadNotificationDTO markReadNotificationDTO) throws Exception {

    }
}
