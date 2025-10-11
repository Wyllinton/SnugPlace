package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Notification.MarkReadNotificationDTO;
import com.snugplace.demo.DTO.Notification.NotificationDTO;
import com.snugplace.demo.Mappers.NotificationMapper;
import com.snugplace.demo.Model.Enums.TypeNotification;
import com.snugplace.demo.Model.Notification;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.NotificationRepository;
import com.snugplace.demo.Service.MailService;
import com.snugplace.demo.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor

public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository  notificationRepository;
    private final NotificationMapper notificationMapper;
    private final MailService mailService;

    @Override
    public List<NotificationDTO> getNotifications() throws Exception {
        return notificationRepository.findAll()
                .stream().map(notificationMapper::toNotificationDTO)
                .toList();
    }

    @Override
    public int getNumberUnreadNotifications(Long userId) throws Exception {
        return notificationRepository.countByReceiverIdAndReadFalse(userId);
    }

    @Override
    public void markAsRead(MarkReadNotificationDTO dto) throws Exception {
        Notification notification = notificationRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con el id: " + dto.id()));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void sendNotification(User receiver, String title, String message, TypeNotification type) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                .title(title)
                .message(message)
                .typeNotification(type)
                .date(LocalDateTime.now())
                .read(false)
                .build();

        notificationRepository.save(notification);

        if (receiver.getEmail() != null) {
            mailService.sendHtmlEmail(receiver.getEmail(), title, message);
        }
    }
}
