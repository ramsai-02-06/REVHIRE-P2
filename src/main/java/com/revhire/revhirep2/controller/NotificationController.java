//package com.revhire.revhirep2.controller;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.revhire.revhirep2.dto.NotificationDTO;
//import com.revhire.revhirep2.entity.Notification;
//import com.revhire.revhirep2.service.NotificationService;
//
//@RestController
//@RequestMapping("/api/notifications")
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//    public NotificationController(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    // CREATE NOTIFICATION (System/Internal use)
//    @PostMapping
//    public ResponseEntity<Notification> createNotification(
//            @RequestBody Notification notification) {
//
//        return ResponseEntity.ok(
//                notificationService.createNotification(notification)
//        );
//    }
//
//    // GET ALL NOTIFICATIONS FOR USER
//    @PreAuthorize("hasAnyRole('JOB_SEEKER','EMPLOYER')")
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<NotificationDTO>> getUserNotifications(
//            @PathVariable Long userId) {
//
//        return ResponseEntity.ok(
//                notificationService.getUserNotifications(userId)
//        );
//    }
//
//    // GET UNREAD NOTIFICATIONS
//    @GetMapping("/user/{userId}/unread")
//    public ResponseEntity<List<Notification>> getUnreadNotifications(
//            @PathVariable Long userId) {
//
//        return ResponseEntity.ok(
//                notificationService.getUnreadNotifications(userId)
//        );
//    }
//
//    // MARK AS READ
//    @PutMapping("/{notificationId}/read")
//    public ResponseEntity<String> markAsRead(
//            @PathVariable Long notificationId) {
//
//        notificationService.markAsRead(notificationId);
//
//        return ResponseEntity.ok("Notification marked as read");
//    }
//
//    // DELETE NOTIFICATION
//    @DeleteMapping("/{notificationId}")
//    public ResponseEntity<String> deleteNotification(
//            @PathVariable Long notificationId) {
//
//        notificationService.deleteNotification(notificationId);
//        return ResponseEntity.ok("Notification deleted successfully");
//    }
//}

package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revhire.revhirep2.dto.NotificationDTO;
import com.revhire.revhirep2.entity.Notification;
import com.revhire.revhirep2.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService=notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification){

        return ResponseEntity.ok(
                notificationService.createNotification(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId){

        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @PathVariable Long userId){

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markRead(@PathVariable Long id){

        notificationService.markAsRead(id);

        return ResponseEntity.ok("Marked as read");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){

        notificationService.deleteNotification(id);

        return ResponseEntity.ok("Deleted");
    }
}