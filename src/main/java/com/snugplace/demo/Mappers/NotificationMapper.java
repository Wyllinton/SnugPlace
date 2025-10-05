package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.Notification.NotificationDTO;
import com.snugplace.demo.DTO.Notification.MarkReadNotificationDTO;
import com.snugplace.demo.Model.Notification;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationDTO toNotificationDTO(Notification notification);

    List<NotificationDTO> toDTOList(List<Notification> notifications);

    Notification toEntity(NotificationDTO dto);

    List<Notification> toEntityList(List<NotificationDTO> dtos);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message")
    void updateNotificationFromMarkReadDto(MarkReadNotificationDTO dto, @MappingTarget Notification notification);
}
