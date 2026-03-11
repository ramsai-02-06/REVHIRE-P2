package com.revhire.revhirep2.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.revhire.revhirep2.entity.Notification;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.repository.NotificationRepository;
import com.revhire.revhirep2.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void createNotification_success() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        notificationService.createNotification(1L,
                "New Job",
                "You applied successfully",
                "NEW_APPLICATION");

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markAsRead_success() {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setIsRead(false);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L);

        assertTrue(notification.getIsRead());
        verify(notificationRepository, times(1)).save(notification);
    }
}