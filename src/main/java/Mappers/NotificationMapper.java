package Mappers;

import DTO.Comment.CommentDTO;
import DTO.NotificationDTO;
import Model.Comment;
import Model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    Notification toEntity(NotificationDTO notificationDTO);
    NotificationDTO toNotificationDTO(Notification notification);
    
}
