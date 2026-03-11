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
@RequestMapping("/jobseeker")
public class JobSeekerNotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public JobSeekerNotificationController(NotificationService notificationService,
                                            UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/notifications")
    public String viewNotifications(Authentication authentication, Model model) {

        User user = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow();

        List<NotificationDTO> notifications =
                notificationService.getUserNotifications(user.getId());

        model.addAttribute("notifications", notifications);

        return "jobseeker/jobseeker-notifications";
    }

    @PostMapping("/notifications/read/{id}")
    public String markRead(@PathVariable Long id) {

        notificationService.markAsRead(id);

        return "redirect:/jobseeker/notifications";
    }
}