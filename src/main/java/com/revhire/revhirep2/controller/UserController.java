//package com.revhire.revhirep2.controller;
//
//import java.util.Optional;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.revhire.revhirep2.dto.ForgotPasswordDTO;
//import com.revhire.revhirep2.dto.SecurityQuestionDTO;
//import com.revhire.revhirep2.dto.UserRegisterDTO;
//import com.revhire.revhirep2.dto.UserResponseDTO;
//import com.revhire.revhirep2.entity.User;
//import com.revhire.revhirep2.mapper.UserMapper;
//import com.revhire.revhirep2.service.UserService;
//
//import jakarta.validation.Valid;
//
//@Controller
//@RequestMapping("/api/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // -------------------------------
//    // REGISTER USER
//    // -------------------------------
//    @PostMapping("/register")
//    public ResponseEntity<?> register(
//            @Valid @RequestBody UserRegisterDTO dto) {
//
//        try {
//            User user = UserMapper.toEntity(dto);
//            User savedUser = userService.registerUser(user);
//
//            return ResponseEntity.ok(
//                    UserMapper.toResponseDTO(savedUser)
//            );
//
//        } catch (Exception e) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Account not created: " + e.getMessage());
//        }
//    }
//
//    // -------------------------------
//    // GET USER BY ID
//    // -------------------------------
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponseDTO> getById(
//            @PathVariable Long id) {
//
//        User user = userService.getUserById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return ResponseEntity.ok(
//                UserMapper.toResponseDTO(user)
//        );
//    }
//
//    // -------------------------------
//    // GET USER BY EMAIL
//    // -------------------------------
//    @GetMapping("/email")
//    public ResponseEntity<UserResponseDTO> getByEmail(
//    		@RequestParam String email) {
//
//        User user = userService.getUserByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return ResponseEntity.ok(
//                UserMapper.toResponseDTO(user)
//        );
//    }
//
//    @GetMapping("/security-question")
//    public ResponseEntity<?> getSecurityQuestion(
//            @RequestParam String email) {
//
//        Optional<SecurityQuestionDTO> response =
//                userService.getSecurityQuestion(email);
//
//        if (response.isEmpty()) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Invalid email");
//        }
//
//        return ResponseEntity.ok(response.get());
//    }
//    
//    // -------------------------------
//    // DEACTIVATE USER
//    // -------------------------------
//    @PutMapping("/{id}/deactivate")
//    public ResponseEntity<String> deactivateUser(
//            @PathVariable Long id) {
//
//        userService.deactivateUser(id);
//        return ResponseEntity.ok("User deactivated successfully");
//    }
//
//    
//    @PutMapping("/forgot-password")
//    public ResponseEntity<?> forgotPassword(
//            @Valid @RequestBody ForgotPasswordDTO dto) {
//
//        userService.resetPassword(
//                dto.getEmail(),
//                dto.getSecurityAnswer(),
//                dto.getNewPassword()
//        );
//
//        return ResponseEntity.ok("Password reset successful");
//    }
//}


package com.revhire.revhirep2.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.revhire.revhirep2.dto.ForgotPasswordDTO;
import com.revhire.revhirep2.dto.SecurityQuestionDTO;
import com.revhire.revhirep2.dto.UserRegisterDTO;
import com.revhire.revhirep2.dto.UserResponseDTO;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.mapper.UserMapper;
import com.revhire.revhirep2.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;
    private static final Logger logger =
            LogManager.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // REGISTER PAGE (GET)
    // =========================
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDto", new UserRegisterDTO());
        return "register";
    }

    // =========================
    // REGISTER FORM SUBMIT (POST)
    // =========================
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerDto") UserRegisterDTO dto,
            BindingResult result,
            Model model) {

        logger.info("Registration attempt for email: {}", dto.getEmail());

        if (result.hasErrors()) {
            logger.warn("Validation errors during registration");
            return "register";
        }

        try {
            User user = UserMapper.toEntity(dto);
            userService.registerUser(user);

            logger.info("User registered successfully: {}", dto.getEmail());

            return "redirect:/login?registered";

        } catch (Exception e) {

            logger.error("Registration failed: {}", e.getMessage());

            model.addAttribute("errorMessage",
                    "Account not created. Email may already exist.");
            return "register";
        }
    }


    // =========================
    // GET USER BY ID
    // =========================
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {

        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                UserMapper.toResponseDTO(user)
        );
    }

    // =========================
    // GET USER BY EMAIL
    // =========================
    @GetMapping("/email")
    @ResponseBody
    public ResponseEntity<UserResponseDTO> getByEmail(@RequestParam String email) {

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                UserMapper.toResponseDTO(user)
        );
    }

    // =========================
    // GET SECURITY QUESTION
    // =========================
    @GetMapping("/security-question")
    @ResponseBody
    public ResponseEntity<?> getSecurityQuestion(@RequestParam String email) {

        Optional<SecurityQuestionDTO> response =
                userService.getSecurityQuestion(email);

        if (response.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid email");
        }

        return ResponseEntity.ok(response.get());
    }

    // =========================
    // DEACTIVATE USER
    // =========================
    @PutMapping("/{id}/deactivate")
    @ResponseBody
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {

        userService.deactivateUser(id);
        return ResponseEntity.ok("User deactivated successfully");
    }

    // =========================
    // RESET PASSWORD
    // =========================
    @PutMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDTO dto) {

        userService.resetPassword(
                dto.getEmail(),
                dto.getSecurityAnswer(),
                dto.getNewPassword()
        );

        return ResponseEntity.ok("Password reset successful");
    }
}