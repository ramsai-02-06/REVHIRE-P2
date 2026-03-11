package com.revhire.revhirep2.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revhire.revhirep2.dto.NotificationDTO;
import com.revhire.revhirep2.entity.Notification;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.repository.NotificationRepository;
import com.revhire.revhirep2.repository.UserRepository;
import com.revhire.revhirep2.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository){
        this.notificationRepository=notificationRepository;
        this.userRepository=userRepository;
    }

    @Override
    public Notification createNotification(Notification notification){

        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }

    @Override
    public void createNotification(Long userId,String title,String message,String type){

        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

 // GET USER NOTIFICATIONS
    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(n -> {

                    NotificationDTO dto = new NotificationDTO();

                    dto.setId(n.getId());
                    dto.setMessage(n.getMessage());
                    dto.setCreatedAt(n.getCreatedAt());
                 

                    return dto;

                }).collect(Collectors.toList());
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId){

        return notificationRepository
                .findByUserIdAndIsReadFalse(userId);
    }

    @Override
    public Notification markAsRead(Long notificationId){

        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(()->new ResourceNotFoundException("Notification not found"));

        n.setIsRead(true);

        return notificationRepository.save(n);
    }

    
    
    
    
    @Override
    public void deleteNotification(Long notificationId){

        if(!notificationRepository.existsById(notificationId)){
            throw new RuntimeException("Notification not found");
        }

        notificationRepository.deleteById(notificationId);
    }
}