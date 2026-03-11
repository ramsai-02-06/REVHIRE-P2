package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revhire.revhirep2.dto.NotificationDTO;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.service.NotificationService;
import com.revhire.revhirep2.service.UserService;

@Controller
@RequestMapping("/employer/notifications")
public class EmployerNotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public EmployerNotificationController(NotificationService notificationService,
                                           UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public String view(Authentication auth, Model model) {

        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Long userId = user.getId();

        // Full list for the notifications page body
        List<NotificationDTO> notifications =
                notificationService.getUserNotifications(userId);

        // Unread count for the navbar bell badge
        int unreadCount = notificationService.getUnreadNotifications(userId).size();

        model.addAttribute("notifications", notifications);
        model.addAttribute("notificationCount", unreadCount);

        return "employer/employer-notifications";
    }

    @PostMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/employer/notifications";
    }
}