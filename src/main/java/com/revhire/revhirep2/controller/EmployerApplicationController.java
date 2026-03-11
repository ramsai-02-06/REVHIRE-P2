package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revhire.revhirep2.dto.EmployerApplicationViewDTO;
import com.revhire.revhirep2.dto.ResumeDTO;
import com.revhire.revhirep2.entity.Employer;
import com.revhire.revhirep2.service.ApplicationService;
import com.revhire.revhirep2.service.EmployerService;
import com.revhire.revhirep2.service.NotificationService;
import com.revhire.revhirep2.service.ResumeService;

@Controller
@RequestMapping("/employer/applications")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerApplicationController {

    private final ApplicationService service;
    private final EmployerService employerService;
    private final ResumeService resumeService;
    private final NotificationService notificationService;

    public EmployerApplicationController(ApplicationService service,
                                         EmployerService employerService,
                                         ResumeService resumeService,
                                         NotificationService notificationService) {
        this.service = service;
        this.employerService = employerService;
        this.resumeService = resumeService;
        this.notificationService = notificationService;
    }

    // ─────────────────────────────────────────────────────────
    // HELPER — only badge count needed (bell now links to notifications page)
    // ─────────────────────────────────────────────────────────
    private void addNotifAttrs(Employer employer, Model model) {
        Long userId = employer.getUser().getId();
        model.addAttribute("notificationCount",
                notificationService.getUnreadNotifications(userId).size());
    }

    // ================= VIEW APPLICANTS BY JOB =================
    @GetMapping("/job/{jobId}")
    public String viewApplicants(@PathVariable Long jobId,
                                 Authentication auth,
                                 Model model) {
        String email = auth.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        List<EmployerApplicationViewDTO> apps =
                service.getApplicantsForJob(jobId, employer.getId());

        model.addAttribute("applications", apps);
        model.addAttribute("jobId", jobId); // VERY IMPORTANT — kept exactly
        addNotifAttrs(employer, model);
        return "employer/applications";
    }

    // ================= UPDATE STATUS (POST — redirects, no page render) =================
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               Authentication authentication) {
        String email = authentication.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        service.updateStatus(id, employer.getId(), status);
        return "redirect:/employer/applications/all";
    }

    // ================= VIEW ALL APPLICANTS =================
    @GetMapping("/all")
    public String viewAllApplicants(Authentication auth, Model model) {
        String email = auth.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        List<EmployerApplicationViewDTO> apps =
                service.getAllApplicationsForEmployer(employer.getId());

        model.addAttribute("applications", apps);
        addNotifAttrs(employer, model);
        return "employer/applications";
    }

    // ================= VIEW TEXT RESUME =================
    @GetMapping("/resume/{id}")
    public String viewTextResume(@PathVariable Long id,
                                 Authentication auth,
                                 Model model) {
        ResumeDTO resume = resumeService.getResumeById(id);
        if (resume == null) throw new RuntimeException("Resume not found");

        String email = auth.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        model.addAttribute("resume", resume);
        addNotifAttrs(employer, model);
        return "employer/view-resume";
    }

    // ================= VIEW RESUME FILE (redirects, no page render) =================
    @GetMapping("/resume-file/{id}")
    public String openResumeFile(@PathVariable Long id) {
        return "redirect:/files/resume/" + id;
    }
}