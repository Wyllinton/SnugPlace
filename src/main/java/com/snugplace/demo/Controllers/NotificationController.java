package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Notification.MarkReadNotificationDTO;
import com.snugplace.demo.DTO.Notification.NotificationDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.DTO.ResponseListDTO;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseListDTO<List<NotificationDTO>>> getNotifications(@PathVariable Long id) throws Exception {
        List<NotificationDTO> notifications = notificationService.getNotifications();
        return ResponseEntity.ok(new ResponseListDTO<>(false, "Consulta exitosa de lista de notificaciones por usuario", notifications));
    }

    @GetMapping("/user/{id}/unread")
    public ResponseEntity<ResponseDTO<String>> getNumberUnreadNotifications(@PathVariable Long id) throws Exception{
        int notifications = notificationService.getNumberUnreadNotifications(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Notificaciones no leídas " + notifications));
    }

    @PutMapping("/user/{id}/mark-as-read")
    public ResponseEntity<ResponseDTO<String>> markAsRead(@RequestBody MarkReadNotificationDTO markReadNotificationDTO) throws Exception{
        notificationService.markAsRead(markReadNotificationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false,"Notificaciones marcadas como leídas exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> sendNotification(@RequestBody NotificationDTO notificationDTO) throws Exception {
        User receiver = userRepository.findById(notificationDTO.idReceiver())
                .orElseThrow(() -> new RuntimeException("Usuario receptor no encontrado con id: " + notificationDTO.idReceiver()));

        notificationService.sendNotification(
                receiver,
                notificationDTO.title(),
                notificationDTO.message(),
                notificationDTO.type()
        );

        return ResponseEntity.ok(
                new ResponseDTO<>(false, "Notificación enviada correctamente.")
        );
    }
}
