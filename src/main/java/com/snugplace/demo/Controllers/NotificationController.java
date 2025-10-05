package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Notification.MarkReadNotificationDTO;
import com.snugplace.demo.DTO.Notification.NotificationDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    
    @GetMapping
    public ResponseEntity<ResponseDTO<List<NotificationDTO>>> getNotifications() throws Exception{
        //Lógica
        List notifications = new ArrayList<NotificationDTO>();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, notifications));
    }
    
    @GetMapping("/unread/count")
    public ResponseEntity<ResponseDTO<String>> getNumberNotifications() throws Exception{
        //Lógica
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "Número de notificaciones no leídas"));
    }

    @PutMapping("/mark-as-read")
    public ResponseEntity<ResponseDTO<String>> markAsRead(@RequestBody MarkReadNotificationDTO markReadNotificationDTO) throws Exception{
        //Lógica
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "Notificaciones marcadas como leídas exitosamente"));
    }
}
