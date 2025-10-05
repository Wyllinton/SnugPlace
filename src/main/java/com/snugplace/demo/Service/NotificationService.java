package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Notification.MarkReadNotificationDTO;
import com.snugplace.demo.DTO.Notification.NotificationDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotifications() throws Exception;

    int getNumberUnreadNotifications(Long id) throws Exception;

    void markAsRead(MarkReadNotificationDTO markReadNotificationDTO) throws Exception;
}
