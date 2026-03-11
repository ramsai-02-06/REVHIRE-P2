package com.revhire.revhirep2.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.ResumeFileRepository;
import com.revhire.revhirep2.repository.ResumeRepository;
import com.revhire.revhirep2.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeFileRepository resumeFileRepository;

    public CustomAuthenticationSuccessHandler(
            UserRepository userRepository,
            JobSeekerRepository jobSeekerRepository,
            ResumeRepository resumeRepository,
            ResumeFileRepository resumeFileRepository) {

        this.userRepository = userRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.resumeRepository = resumeRepository;
        this.resumeFileRepository = resumeFileRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        boolean isEmployer = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"));

        boolean isJobSeeker = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_JOB_SEEKER"));

        String email = authentication.getName();

        // ================= EMPLOYER =================
        if (isEmployer) {
            response.sendRedirect("/employer/dashboard");
            return;
        }

        // ================= JOB SEEKER =================
        if (isJobSeeker) {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long userId = user.getId();

            // STEP 1 → PROFILE CHECK
            Optional<JobSeeker> optionalJobSeeker =
                    jobSeekerRepository.findByUserId(userId);

            if (optionalJobSeeker.isEmpty()) {
                response.sendRedirect("/jobseeker/complete-profile");
                return;
            }

            JobSeeker jobSeeker = optionalJobSeeker.get();
            Long jobSeekerId = jobSeeker.getId();

            // STEP 2 → TEXT RESUME CHECK
            if (!resumeRepository.existsByJobSeekerId(jobSeekerId)) {
                response.sendRedirect("/jobseeker/create-resume");
                return;
            }

            // STEP 3 → FILE RESUME CHECK
            if (!resumeFileRepository.existsByJobSeekerId(jobSeekerId)) {
                response.sendRedirect("/jobseeker/upload-resume");
                return;
            }

            // ALL COMPLETE
            response.sendRedirect("/jobseeker/dashboard");
        }
    }
}