package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.NotificationDTO;
import com.revhire.revhirep2.entity.Notification;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification n){

        NotificationDTO dto = new NotificationDTO();

        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setMessage(n.getMessage());
        dto.setType(n.getType());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());

        return dto;
    }
}