package Mappers;

import DTO.Comment.CommentDTO;
import DTO.Notification.NotificationDTO;
import Model.Comment;
import Model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    Notification toEntity(NotificationDTO notificationDTO);
    NotificationDTO toNotificationDTO(Notification notification);
    
}
