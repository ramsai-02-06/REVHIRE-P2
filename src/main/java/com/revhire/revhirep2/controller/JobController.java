package com.revhire.revhirep2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revhire.revhirep2.dto.EmployerDashboardDTO;
import com.revhire.revhirep2.dto.JobCreateDTO;
import com.revhire.revhirep2.dto.JobResponseDTO;
import com.revhire.revhirep2.entity.Employer;
import com.revhire.revhirep2.entity.Job;
import com.revhire.revhirep2.mapper.JobMapper;
import com.revhire.revhirep2.service.EmployerService;
import com.revhire.revhirep2.service.JobService;
import com.revhire.revhirep2.service.NotificationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employer/jobs")
public class JobController {

    private final JobService jobService;
    private final EmployerService employerService;
    private final NotificationService notificationService;

    public JobController(JobService jobService,
                         EmployerService employerService,
                         NotificationService notificationService) {
        this.jobService = jobService;
        this.employerService = employerService;
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

    // =========================
    // SHOW POST JOB PAGE
    // =========================
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/create")
    public String showCreateJobPage(Authentication authentication, Model model) {
        String email = authentication.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        model.addAttribute("job", new JobCreateDTO());
        addNotifAttrs(employer, model);
        return "employer/post-job";
    }

    // =========================
    // CREATE JOB (POST — no page render, no notif attrs needed)
    // =========================
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/create")
    public String createJob(@Valid @ModelAttribute("job") JobCreateDTO dto,
                            Authentication authentication) {
        String email = authentication.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        var job = JobMapper.toEntity(dto);
        job.setEmployer(employer);
        jobService.createJob(job);
        return "redirect:/employer/dashboard";
    }

    // =========================
    // EMPLOYER DASHBOARD (via /employer/jobs/dashboard — keep for compat)
    // =========================
    @GetMapping("/dashboard")
    public String employerDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        EmployerDashboardDTO dashboard = employerService.getDashboardByEmail(email);
        List<JobResponseDTO> recentJobs = jobService.getRecentJobsByEmployer(employer.getId());

        model.addAttribute("employer", employer);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("recentJobs", recentJobs);
        addNotifAttrs(employer, model);
        return "employer-dashboard";
    }

    // =========================
    // MANAGE JOBS
    // =========================
    @GetMapping("/manage")
    public String manageJobs(Authentication authentication, Model model) {
        String email = authentication.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        List<Job> jobs = jobService.getByEmployerId(employer.getId());
        model.addAttribute("jobs", jobs);
        addNotifAttrs(employer, model);
        return "manage-jobs";
    }

    // =========================
    // GET JOBS BY EMPLOYER (list — no auth needed, no notif)
    // =========================
    @GetMapping("/list/{employerId}")
    public String getJobsByEmployer(@PathVariable Long employerId, Model model) {
        List<JobResponseDTO> jobs = jobService.getByEmployerId(employerId)
                .stream().map(JobMapper::toDTO).collect(Collectors.toList());
        model.addAttribute("jobs", jobs);
        return "employer/manage-jobs";
    }

    // =========================
    // EDIT JOB PAGE
    // =========================
    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable Long id,
                          Authentication authentication,
                          Model model) {
        Job job = jobService.getById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String email = authentication.getName();
        Employer employer = employerService.getByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        model.addAttribute("job", job);
        addNotifAttrs(employer, model);
        return "employer/edit-job";
    }

    // =========================
    // UPDATE JOB (POST — redirects, no page render)
    // =========================
    @PostMapping("/update/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute Job job) {
        jobService.updateJob(id, job);
        return "redirect:/employer/dashboard";
    }

    // =========================
    // DELETE JOB — form uses method="post" so this must be @PostMapping
    // =========================
    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "redirect:/employer/dashboard";
    }
}