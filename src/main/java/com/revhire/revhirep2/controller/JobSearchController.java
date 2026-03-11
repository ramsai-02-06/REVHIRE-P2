package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revhire.revhirep2.dto.ApplicationCreateDTO;
import com.revhire.revhirep2.dto.JobResponseDTO;
import com.revhire.revhirep2.dto.ResumeFileDTO;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.mapper.JobMapper;
import com.revhire.revhirep2.service.ApplicationService;
import com.revhire.revhirep2.service.JobSeekerService;
import com.revhire.revhirep2.service.JobService;
import com.revhire.revhirep2.service.ResumeFileService;
import com.revhire.revhirep2.service.ResumeService;
import com.revhire.revhirep2.service.UserService;

@Controller
@RequestMapping("/jobs")
public class JobSearchController {

    private final JobService jobService;
    private final UserService userService;
    private final JobSeekerService jobSeekerService;
    private final ResumeService resumeService;
    private final ResumeFileService resumeFileService;
    private final ApplicationService applicationService;

    public JobSearchController(JobService jobService,
                               UserService userService,
                               JobSeekerService jobSeekerService,
                               ResumeService resumeService,
                               ResumeFileService resumeFileService,
                               ApplicationService applicationService) {
        this.jobService = jobService;
        this.userService = userService;
        this.jobSeekerService = jobSeekerService;
        this.resumeService = resumeService;
        this.resumeFileService = resumeFileService;
        this.applicationService = applicationService;
    }

    // ==============================
    // VIEW ALL ACTIVE JOBS
    // ==============================

    @GetMapping("/search")
    public String browseJobs(
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) Integer openings,
            @RequestParam(required = false) String jobType,
            Model model) {

        List<JobResponseDTO> jobs =
                jobService.getAllActiveJobs()
                        .stream()
                        .filter(job -> experience == null ||
                                job.getExperienceRequired() <= experience)

                        .filter(job -> openings == null ||
                                job.getOpenings() >= openings)

                        .filter(job -> jobType == null ||
                                jobType.isEmpty() ||
                                job.getJobType().equalsIgnoreCase(jobType))

                        .map(JobMapper::toDTO)
                        .toList();

        model.addAttribute("jobs", jobs);

        return "jobs/search-jobs";
    }
    
    @GetMapping("/apply-form/{jobId}")
    public String showApplyForm(@PathVariable Long jobId, Model model) {

        model.addAttribute("jobId", jobId);
        model.addAttribute("application", new ApplicationCreateDTO());

        return "jobs/apply-job";
    }
    // ==============================
    // APPLY JOB
    // ==============================

    @PostMapping("/apply")
    public String submitApplication(@ModelAttribute ApplicationCreateDTO dto,
                                    Authentication authentication,
                                    Model model){

        try {

            String email = authentication.getName();

            User user = userService.getUserByEmail(email).orElseThrow();

            JobSeeker jobSeeker =
                    jobSeekerService.getByUserId(user.getId()).orElseThrow();

            Resume resume =
                    resumeService.getByJobSeekerId(jobSeeker.getId()).orElseThrow();

            ResumeFileDTO resumeFile =
                    resumeFileService.getByJobSeekerId(jobSeeker.getId());

            dto.setJobSeekerId(jobSeeker.getId());
            dto.setResumeId(resume.getId());
            dto.setResumeFileId(resumeFile.getId());

            applicationService.apply(dto);

            model.addAttribute("successMessage", "Application submitted successfully");

            return "jobs/application-success";

        } catch (Exception e) {

            model.addAttribute("errorMessage", "You have already applied for this job");

            return "jobs/application-success";
        }
    }
    
    
//    @GetMapping("/apply/{jobId}")
//    public String applyJob(@PathVariable Long jobId,
//                           Authentication authentication,
//                           Model model){
//
//        try {
//
//            String email = authentication.getName();
//
//            User user = userService.getUserByEmail(email).orElseThrow();
//
//            JobSeeker jobSeeker =
//                    jobSeekerService.getByUserId(user.getId()).orElseThrow();
//
//            Resume resume =
//                    resumeService.getByJobSeekerId(jobSeeker.getId()).orElseThrow();
//
//            ResumeFileDTO resumeFile =
//                    resumeFileService.getByJobSeekerId(jobSeeker.getId());
//
//            ApplicationCreateDTO dto = new ApplicationCreateDTO();
//
//            dto.setJobId(jobId);
//            dto.setJobSeekerId(jobSeeker.getId());
//            dto.setResumeId(resume.getId());
//            dto.setResumeFileId(resumeFile.getId());
//
//            applicationService.apply(dto);
//
//            return "redirect:/jobseeker/dashboard?success=applied";
//
//        } catch (Exception e) {
//
//            return "redirect:/jobs/search?error=already_applied";
//        }
//    }

    
    
 
}