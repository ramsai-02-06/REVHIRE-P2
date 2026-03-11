package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.revhire.revhirep2.dto.ApplicationResponseDTO;
import com.revhire.revhirep2.dto.JobSeekerDTO;
import com.revhire.revhirep2.dto.ResumeDTO;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.SavedJob;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.mapper.JobSeekerMapper;
import com.revhire.revhirep2.mapper.ResumeMapper;
import com.revhire.revhirep2.service.ApplicationService;
import com.revhire.revhirep2.service.JobSeekerService;
import com.revhire.revhirep2.service.JobService;
import com.revhire.revhirep2.service.ResumeFileService;
import com.revhire.revhirep2.service.ResumeService;
import com.revhire.revhirep2.service.SavedJobService;
import com.revhire.revhirep2.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/jobseeker")
@PreAuthorize("hasRole('JOB_SEEKER')")
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;
    private final UserService userService;
    private final ResumeService resumeService;
    private final ResumeFileService resumeFileService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final JobService jobService;
    
    public JobSeekerController(JobSeekerService jobSeekerService,
            UserService userService,
            ResumeService resumeService,
            ResumeFileService resumeFileService,
            ApplicationService applicationService,
            SavedJobService savedJobService,
            JobService jobService) {
this.jobSeekerService = jobSeekerService;
this.userService = userService;
this.resumeService = resumeService;
this.resumeFileService = resumeFileService;
this.applicationService = applicationService;
this.savedJobService = savedJobService;
this.jobService=jobService;
}

    // =========================================================
    // 1️⃣ COMPLETE PROFILE
    // =========================================================

    @GetMapping("/complete-profile")
    public String showCompleteProfile(Model model) {
        model.addAttribute("jobSeeker", new JobSeekerDTO());
        return "complete-profile";
    }

    @PostMapping("/save-profile")
    public String saveProfile(@Valid @ModelAttribute("jobSeeker") JobSeekerDTO dto,
                              BindingResult result,
                              Authentication authentication) {

        if (result.hasErrors()) {
            return "complete-profile";
        }

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        jobSeekerService.createProfile(user.getId(), dto);

        return "redirect:/jobseeker/create-resume";
    }

    // =========================================================
    // 2️⃣ TEXT RESUME
    // =========================================================

    @GetMapping("/create-resume")
    public String showCreateResume(Model model) {
        model.addAttribute("resume", new ResumeDTO());
        return "create-resume";
    }

    @PostMapping("/save-resume")
    public String saveResume(@Valid @ModelAttribute("resume") ResumeDTO dto,
                             BindingResult result,
                             Authentication authentication) {

        if (result.hasErrors()) {
            return "create-resume";
        }

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService.getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        resumeService.createResumeFromDTO(jobSeeker.getId(), dto);

        return "redirect:/jobseeker/upload-resume";
    }

    // =========================================================
    // 3️⃣ UPLOAD RESUME FILE
    // =========================================================

    @GetMapping("/upload-resume")
    public String showUploadResume() {
        return "upload-resume";
    }

    @PostMapping("/save-resume-file")
    public String uploadResumeFile(@RequestParam("file") MultipartFile file,
                                   Authentication authentication) throws Exception {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService.getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        resumeFileService.uploadFile(jobSeeker.getId(), file);

        return "redirect:/jobseeker/dashboard";
    }

    // =========================================================
    // 4️⃣ DASHBOARD
    // =========================================================

//    @GetMapping("/dashboard")
//    public String jobSeekerDashboard(Authentication authentication,
//                                     Model model) {
//
//        String email = authentication.getName();
//
//        User user = userService.getUserByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        JobSeeker jobSeeker = jobSeekerService
//                .getByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Profile not found"));
//
//        model.addAttribute("jobSeeker", jobSeeker);
//        model.addAttribute("email", email);   // 🔥 ADD THIS
//        model.addAttribute("resumeFile",
//                resumeFileService.getByJobSeekerId(jobSeeker.getId()));
//        
//        
//        return "jobseeker-dashboard";
//    }
    
    @GetMapping("/dashboard")
    public String jobSeekerDashboard(Authentication authentication,
                                     Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Username
        model.addAttribute("username", user.getEmail());

        // Resume File
        model.addAttribute("resumeFile",
                resumeFileService.getByJobSeekerId(jobSeeker.getId()));

        // Applications
        List<ApplicationResponseDTO> applications =
                applicationService.getMyApplications(jobSeeker.getId());

        model.addAttribute("applications", applications);
        model.addAttribute("appliedJobs", applications.size());

        // Saved Jobs
        List<SavedJob> savedJobs =
                savedJobService.getSavedJobs(jobSeeker.getId());

        model.addAttribute("savedJobs", savedJobs.size());

        // Latest Jobs
        model.addAttribute("jobs",
                jobService.getAllActiveJobs());

        // Simple profile completion logic
        int profileCompletion = 60;

        if(jobSeeker.getFullName()!=null) profileCompletion+=10;
        if(jobSeeker.getPhone()!=null) profileCompletion+=10;
        if(jobSeeker.getLocation()!=null) profileCompletion+=10;
        if(jobSeeker.getProfileSummary()!=null) profileCompletion+=10;

        model.addAttribute("profileCompletion", profileCompletion);

        return "jobseeker-dashboard";
    }
    
 // =========================================================
    // 5️⃣ VIEW PROFILE
    // =========================================================

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication,
                              Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        model.addAttribute("profile", jobSeeker);

        return "jobseeker/view-profile";
    }
 // ================= VIEW TEXT RESUME =================

    @GetMapping("/view-text-resume")
    public String viewTextResume(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Resume resume = resumeService
                .getByJobSeekerId(jobSeeker.getId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        model.addAttribute("resume", ResumeMapper.toDTO(resume));

        return "jobseeker/view-text-resume";
    }


    
 // =========================
 // VIEW TEXT RESUME PAGE
 // =========================

 @GetMapping("/resume")
 public String viewResume(Authentication authentication, Model model) {

     String email = authentication.getName();

     User user = userService.getUserByEmail(email)
             .orElseThrow(() -> new RuntimeException("User not found"));

     JobSeeker jobSeeker = jobSeekerService
             .getByUserId(user.getId())
             .orElseThrow(() -> new RuntimeException("Profile not found"));

     Resume resume = resumeService
             .getByJobSeekerId(jobSeeker.getId())
             .orElseThrow(() -> new RuntimeException("Resume not found"));

     model.addAttribute("resume", ResumeMapper.toDTO(resume));

     return "jobseeker/view-text-resume";
 }
    // ================= UPDATE RESUME PAGE =================

    @GetMapping("/update-text-resume")
    public String updateTextResume(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Resume resume = resumeService
                .getByJobSeekerId(jobSeeker.getId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        model.addAttribute("resume", ResumeMapper.toDTO(resume));

        return "jobseeker/update-text-resume";
    }


    // ================= SAVE UPDATED RESUME =================

    @PostMapping("/update-resume")
    public String updateResume(@ModelAttribute("resume") ResumeDTO dto) {

        Resume resume = ResumeMapper.toEntity(dto);

        resumeService.updateResume(dto.getId(), resume);

        return "redirect:/jobseeker/view-text-resume";
    }
    // =========================================================
    // 7️⃣ VIEW UPLOADED RESUME
    // =========================================================

    @GetMapping("/resume-file")
    public String viewUploadedResume(Authentication authentication,
                                     Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        model.addAttribute("resumeFile",
                resumeFileService.getByJobSeekerId(jobSeeker.getId()));
        return "jobseeker/view-uploaded-resume";
    }

    @GetMapping("/update-resume")
    public String updateResumePage(Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService
                .getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("JobSeeker profile not found"));

        model.addAttribute("jobSeekerId", jobSeeker.getId());

        return "jobseeker/update-resume";
    }
    
 // =========================================================
 // 9️⃣ SHOW UPDATE PROFILE PAGE
 // =========================================================

    @GetMapping("/update-profile")
    public String showUpdateProfile(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService.getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        JobSeekerDTO dto = JobSeekerMapper.toDTO(jobSeeker);

        model.addAttribute("jobSeeker", dto);

        return "jobseeker/update-profile";
    }
 // =========================================================
 // 🔟 UPDATE PROFILE
 // =========================================================

    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("jobSeeker") JobSeekerDTO dto,
                                BindingResult result,
                                Authentication authentication) {

        if (result.hasErrors()) {
            return "jobseeker/update-profile";
        }

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = jobSeekerService
                .getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        jobSeekerService.updateProfile(jobSeeker.getId(), dto);

        return "redirect:/jobseeker/dashboard";
    }
    
 // =========================================================
 // 11️⃣ MY APPLICATIONS
 // =========================================================

 @GetMapping("/applications")
 public String myApplications(Authentication authentication,
                              Model model) {

     String email = authentication.getName();

     User user = userService
             .getUserByEmail(email)
             .orElseThrow(() -> new RuntimeException("User not found"));

     JobSeeker jobSeeker = jobSeekerService
             .getByUserId(user.getId())
             .orElseThrow(() -> new RuntimeException("JobSeeker profile not found"));

     List<ApplicationResponseDTO> applications =
             applicationService.getMyApplications(jobSeeker.getId());

     model.addAttribute("applications", applications);

     return "jobseeker/my-applications";
 }


 // =========================================================
 // 12️⃣ WITHDRAW APPLICATION
 // =========================================================

 @GetMapping("/withdraw/{applicationId}")
 public String withdrawApplication(@PathVariable Long applicationId,
                                   Authentication authentication) {

     String email = authentication.getName();

     User user = userService
             .getUserByEmail(email)
             .orElseThrow(() -> new RuntimeException("User not found"));

     JobSeeker jobSeeker = jobSeekerService
             .getByUserId(user.getId())
             .orElseThrow(() -> new RuntimeException("JobSeeker profile not found"));

     applicationService.withdraw(applicationId, jobSeeker.getId());

     return "redirect:/jobseeker/applications";
 }
 // =========================================================
 // SAVED JOBS
 // =========================================================

 @GetMapping("/saved-jobs")
 public String viewSavedJobs(Authentication authentication,
                             Model model) {

     String email = authentication.getName();

     User user = userService.getUserByEmail(email)
             .orElseThrow(() -> new RuntimeException("User not found"));

     JobSeeker jobSeeker = jobSeekerService
             .getByUserId(user.getId())
             .orElseThrow(() -> new RuntimeException("Profile not found"));

     List<SavedJob> savedJobs =
             savedJobService.getSavedJobs(jobSeeker.getId());

     model.addAttribute("savedJobs", savedJobs);

     return "jobseeker/saved-jobs";
 }

 // =========================================================
 // SAVE JOB
 // =========================================================

 @PostMapping("/save-job/{jobId}")
 public String saveJob(@PathVariable Long jobId,
                       Authentication authentication) {

     String email = authentication.getName();

     User user = userService.getUserByEmail(email)
             .orElseThrow(() -> new RuntimeException("User not found"));

     JobSeeker jobSeeker = jobSeekerService
             .getByUserId(user.getId())
             .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

     try {

         savedJobService.saveJob(jobId, jobSeeker.getId());

         return "redirect:/jobs/search?saved=true";

     } catch (Exception e) {

         return "redirect:/jobs/search?alreadySaved=true";
     }
 }

 // =========================================================
 // REMOVE SAVED JOB
 // =========================================================

 @PostMapping("/remove-saved-job/{jobId}")
 public String removeSavedJob(@PathVariable Long jobId,
                              Authentication authentication) {

     String email = authentication.getName();

     User user = userService.getUserByEmail(email)
             .orElseThrow(() -> new RuntimeException("User not found"));

     JobSeeker jobSeeker = jobSeekerService
             .getByUserId(user.getId())
             .orElseThrow(() -> new RuntimeException("Profile not found"));

     savedJobService.removeSavedJob(jobId, jobSeeker.getId());

     return "redirect:/jobseeker/saved-jobs";
 }

    
 
    // =========================================================
    // 8️⃣ LOGOUT SUCCESS
    // =========================================================

    
    
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout-success";
    }

}