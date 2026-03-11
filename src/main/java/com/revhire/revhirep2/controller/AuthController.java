package com.revhire.revhirep2.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.revhire.revhirep2.dto.ForgotPasswordDTO;
import com.revhire.revhirep2.dto.SecurityQuestionDTO;
import com.revhire.revhirep2.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    private static final Logger logger =
            LogManager.getLogger(AuthController.class);

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ================= LOGIN PAGE =================
    @GetMapping("/login")
    public String loginPage() {
        logger.info("Login page requested");
        return "login";
    }

    // ================= FORGOT PASSWORD PAGE =================
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {

        logger.info("Forgot password page requested");

        model.addAttribute("forgotDto", new ForgotPasswordDTO());

        return "forgot-password";
    }

    // ================= LOAD SECURITY QUESTION =================
    @PostMapping("/forgot-password")
    public String loadSecurityQuestion(
            @ModelAttribute("forgotDto") ForgotPasswordDTO dto,
            Model model) {

        logger.info("Forgot password request for email: {}", dto.getEmail());

        Optional<SecurityQuestionDTO> questionOpt =
                userService.getSecurityQuestion(dto.getEmail());

        if (questionOpt.isEmpty()) {

            logger.warn("Invalid email entered: {}", dto.getEmail());

            model.addAttribute("errorMessage", "Invalid email address");
            return "forgot-password";
        }

        logger.info("Security question loaded for email: {}", dto.getEmail());

        model.addAttribute("securityQuestion",
                questionOpt.get().getSecurityQuestion());

        model.addAttribute("resetDto", dto);

        return "reset-password";
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public String resetPassword(
            @ModelAttribute("resetDto") ForgotPasswordDTO dto,
            Model model) {

        logger.info("Reset password attempt for email: {}", dto.getEmail());

        try {

            userService.resetPassword(
                    dto.getEmail(),
                    dto.getSecurityAnswer(),
                    dto.getNewPassword()
            );

            logger.info("Password reset successful for {}", dto.getEmail());

            // SUCCESS MESSAGE
            model.addAttribute("successMessage",
                    "Password updated successfully");

            model.addAttribute("securityQuestion",
                    userService.getSecurityQuestion(dto.getEmail())
                               .get()
                               .getSecurityQuestion());

            return "reset-password";

        } catch (Exception e) {

            logger.error("Password reset failed for {}", dto.getEmail());

            model.addAttribute("errorMessage",
                    "Invalid security answer");

            model.addAttribute("securityQuestion",
                    userService.getSecurityQuestion(dto.getEmail())
                               .get()
                               .getSecurityQuestion());

            return "reset-password";
        }
    }
}