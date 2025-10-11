package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Notification.MarkReadNotificationDTO;
import com.snugplace.demo.DTO.Notification.NotificationDTO;
import com.snugplace.demo.Model.Enums.TypeNotification;
import com.snugplace.demo.Model.Notification;
import com.snugplace.demo.Model.User;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotifications() throws Exception;

    int getNumberUnreadNotifications(Long id) throws Exception;

    void markAsRead(MarkReadNotificationDTO markReadNotificationDTO) throws Exception;

    void sendNotification(User receiver, String title, String message, TypeNotification type) throws Exception;
}
