package com.revhire.revhirep2.service;

import com.revhire.revhirep2.dto.NotificationDTO;
import com.revhire.revhirep2.entity.Notification;
import java.util.List;

public interface NotificationService {

    Notification createNotification(Notification notification);

    void createNotification(Long userId,String title,String message,String type);

    List<NotificationDTO> getUserNotifications(Long userId);

    List<Notification> getUnreadNotifications(Long userId);

    Notification markAsRead(Long notificationId);

    void deleteNotification(Long notificationId);
}