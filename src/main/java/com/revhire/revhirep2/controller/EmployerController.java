//package com.revhire.revhirep2.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.revhire.revhirep2.dto.EmployerDTO;
//import com.revhire.revhirep2.entity.Employer;
//import com.revhire.revhirep2.entity.User;
//import com.revhire.revhirep2.exception.ResourceNotFoundException;
//import com.revhire.revhirep2.mapper.EmployerMapper;
//import com.revhire.revhirep2.service.EmployerService;
//import com.revhire.revhirep2.service.UserService;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/employer")
//public class EmployerController {
//
//    private final EmployerService employerService;
//    private final UserService userService;
//
//    public EmployerController(EmployerService employerService,
//                              UserService userService) {
//        this.employerService = employerService;
//        this.userService = userService;
//    }
//
//    
//    // ============================================================
//    // CREATE EMPLOYER PROFILE
//    // ============================================================
//
//    @PreAuthorize("hasRole('EMPLOYER')")
//    @PostMapping("/create/{userId}")
//    public ResponseEntity<EmployerDTO> createEmployer(
//            @PathVariable Long userId,
//            @Valid @RequestBody EmployerDTO dto) {
//
//        User user = userService.getUserById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Employer employer = EmployerMapper.toEntity(dto);
//        employer.setUser(user);
//
//        Employer saved = employerService.createEmployer(employer);
//
//        return ResponseEntity.ok(EmployerMapper.toDTO(saved));
//    }
//
//    // ============================================================
//    // UPDATE EMPLOYER PROFILE
//    // ============================================================
//
////    @PreAuthorize("hasRole('EMPLOYER')")
////    @PutMapping("/update/{id}")
////    public ResponseEntity<EmployerDTO> updateEmployer(
////            @PathVariable Long id,
////            @Valid @RequestBody EmployerDTO dto) {
////
////        Employer employer = EmployerMapper.toEntity(dto);
////
////        Employer updated = employerService.updateEmployer(id, employer);
////
////        return ResponseEntity.ok(EmployerMapper.toDTO(updated));
////    }
//
//    
// // ✅ GET PROFILE (Render existing data)
//    @PreAuthorize("hasRole('EMPLOYER')")
//    @GetMapping("/{id}")
//    public ResponseEntity<EmployerDTO> getProfile(
//            @PathVariable Long id) {
//
//        return ResponseEntity.ok(
//                employerService.getProfile(id)
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException("Employer not found"))
//        );
//    }
//
//    // ✅ UPDATE PROFILE
//    @PreAuthorize("hasRole('EMPLOYER')")
//    @PutMapping("/{id}")
//    public ResponseEntity<EmployerDTO> updateProfile(
//            @PathVariable Long id,
//            @Valid @RequestBody EmployerDTO dto) {
//
//        return ResponseEntity.ok(
//                employerService.updateProfile(id, dto)
//        );
//    }
//    // ============================================================
//    // GET EMPLOYER BY USER ID
//    // ============================================================
//
//    @PreAuthorize("hasRole('EMPLOYER')")
//    @GetMapping("/by-user/{userId}")
//    public ResponseEntity<EmployerDTO> getByUserId(
//            @PathVariable Long userId) {
//
//        Employer employer = employerService.getByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Employer profile not found"));
//
//        return ResponseEntity.ok(EmployerMapper.toDTO(employer));
//    }
//
//    // ============================================================
//    // GET EMPLOYER BY EMPLOYER ID
//    // ============================================================
//
//    @PreAuthorize("hasRole('EMPLOYER')")
//    @GetMapping("/by-id/{id}")
//    public ResponseEntity<EmployerDTO> getById(
//            @PathVariable Long id) {
//
//        Employer employer = employerService.getById(id)
//                .orElseThrow(() -> new RuntimeException("Employer not found"));
//
//        return ResponseEntity.ok(EmployerMapper.toDTO(employer));
//    }
//    
//    
//    @GetMapping("/dashboard")
//    public String employerDashboard() {
//        return "employer-dashboard";
//    }
////    @GetMapping("/dashboard")
////    @PreAuthorize("hasRole('EMPLOYER')")
////    public ResponseEntity<EmployerDashboardDTO> getDashboard(
////            Authentication authentication) {
////
////        String email = authentication.getName();
////
////        return ResponseEntity.ok(
////                employerService.getDashboardByEmail(email)
////        );
////    }
//}

package com.revhire.revhirep2.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revhire.revhirep2.dto.EmployerApplicationViewDTO;
import com.revhire.revhirep2.dto.EmployerDTO;
import com.revhire.revhirep2.dto.EmployerDashboardDTO;
import com.revhire.revhirep2.dto.JobResponseDTO;
import com.revhire.revhirep2.entity.Employer;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.mapper.EmployerMapper;
import com.revhire.revhirep2.service.ApplicationService;
import com.revhire.revhirep2.service.EmployerService;
import com.revhire.revhirep2.service.JobService;
import com.revhire.revhirep2.service.NotificationService;
import com.revhire.revhirep2.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employer")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerController {

    private static final Logger logger =
            LogManager.getLogger(EmployerController.class);

    private final EmployerService employerService;
    private final UserService userService;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final ApplicationService applicationService;
    
    public EmployerController(EmployerService employerService,
            UserService userService,
            JobService jobService,NotificationService notificationService,
    	ApplicationService applicationService) {
this.employerService = employerService;
this.userService = userService;
this.jobService = jobService;
this.notificationService = notificationService;
this.applicationService = applicationService;
    }

    
    @GetMapping("/complete-profile")
    public String showCompleteProfile(Model model) {
        model.addAttribute("employer", new EmployerDTO());
        return "employer-complete-profile";
    }
    
    @PostMapping("/complete-profile")
    public String saveCompleteProfile(@ModelAttribute EmployerDTO dto,
                                      Authentication authentication) {

        String email = authentication.getName();

        Employer employer = employerService
                .getByUserEmail(email)
                .orElse(null);

        if (employer == null) {

            // CREATE NEW EMPLOYER
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Employer newEmployer = EmployerMapper.toEntity(dto);
            newEmployer.setUser(user);

            employerService.createEmployer(newEmployer);

        } else {

            // UPDATE EXISTING EMPLOYER
            employerService.updateByEmail(email, dto);
        }

        return "redirect:/employer/dashboard";
    }
    // ============================================================
    // DASHBOARD
    // ============================================================

    
    @GetMapping("/dashboard")
    public String employerDashboard(Authentication authentication, Model model) {

        String email = authentication.getName();

        // check if employer profile exists
        Employer employer = employerService
                .getByUserEmail(email)
                .orElse(null);

        // if profile not created -> redirect
        if (employer == null) {
            return "redirect:/employer/complete-profile";
        }

        EmployerDTO employerDTO = EmployerMapper.toDTO(employer);

        EmployerDashboardDTO dashboard =
                employerService.getDashboardByEmail(email);

        List<JobResponseDTO> recentJobs =
                jobService.getRecentJobsByEmployer(employer.getId());

        Long userId = employer.getUser().getId();

        long notificationCount =
                notificationService.getUnreadNotifications(userId).size();

        model.addAttribute("dashboard", dashboard);
        model.addAttribute("recentJobs", recentJobs);
        model.addAttribute("employer", employerDTO);
        model.addAttribute("notificationCount", notificationCount);

        return "employer-dashboard";
    }
    
    
    
    // ============================================================
    // CREATE PROFILE (NO USER ID NEEDED)
    // ============================================================

    @PostMapping("/create")
    public String createEmployer(Authentication authentication,
                                 @Valid @ModelAttribute EmployerDTO dto) {

        String email = authentication.getName();
        logger.info("Employer profile creation request by {}", email);

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Employer employer = EmployerMapper.toEntity(dto);
        employer.setUser(user);

        employerService.createEmployer(employer);

        logger.info("Employer profile created successfully for {}", email);

        return "redirect:/employer/dashboard";
    }

    // ============================================================
    // GET PROFILE
    // ============================================================

    @GetMapping("/profile")
    public String getProfile(Authentication authentication,
                             Model model) {

        String email = authentication.getName();
        logger.info("Fetching employer profile for {}", email);

        Employer employer = employerService
                .getByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employer not found"));

        // Navbar bell badge count only
        Long userId = employer.getUser().getId();
        model.addAttribute("notificationCount",
                notificationService.getUnreadNotifications(userId).size());

        model.addAttribute("employer", EmployerMapper.toDTO(employer));

        return "employer-profile";
    }

    // ============================================================
    // UPDATE PROFILE
    // ============================================================

    @PostMapping("/update")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute EmployerDTO dto) {

        String email = authentication.getName();
        logger.info("Employer profile update requested by {}", email);

        employerService.updateByEmail(email, dto);

        logger.info("Employer profile updated successfully for {}", email);

        return "redirect:/employer/dashboard";
    }

    // ============================================================
    // GET BY USER EMAIL (Internal)
    // ============================================================

    @GetMapping("/details")
    @ResponseBody
    public EmployerDTO getEmployerDetails(Authentication authentication) {

        String email = authentication.getName();
        logger.info("Employer details API called by {}", email);

        Employer employer = employerService
                .getByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employer not found"));

        return EmployerMapper.toDTO(employer);
    }
    
    // ======================================
    // VIEW ALL APPLICANTS FOR EMPLOYER
    // ======================================

    @GetMapping("/applicants")
    public String viewApplicants(Authentication authentication,
                                 Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employer employer = employerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        List<EmployerApplicationViewDTO> applications =
                applicationService.getAllApplicationsForEmployer(employer.getId());

        model.addAttribute("applications", applications);

        return "employer/applicants";
    }

    // ======================================
    // UPDATE APPLICATION STATUS
    // ======================================

    @PostMapping("/applications/update-status")
    public String updateApplicationStatus(@RequestParam Long applicationId,
                                          @RequestParam String status,
                                          Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employer employer = employerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        applicationService.updateStatus(applicationId, employer.getId(), status);

        return "redirect:/employer/applicants";
    }


    
//    @PostMapping("/notifications/read/{id}")
//    public String markNotificationRead(@PathVariable Long id){
//
//        notificationService.markAsRead(id);
//
//        return "redirect:/employer/notifications";
//    }
//    
}