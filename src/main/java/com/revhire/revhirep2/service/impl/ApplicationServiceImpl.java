package com.revhire.revhirep2.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revhire.revhirep2.dto.ApplicationCreateDTO;
import com.revhire.revhirep2.dto.ApplicationResponseDTO;
import com.revhire.revhirep2.dto.EmployerApplicationViewDTO;
import com.revhire.revhirep2.entity.Application;
import com.revhire.revhirep2.entity.Job;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.ResumeFile;
import com.revhire.revhirep2.mapper.ApplicationMapper;
import com.revhire.revhirep2.mapper.ResumeFileMapper;
import com.revhire.revhirep2.mapper.ResumeMapper;
import com.revhire.revhirep2.repository.ApplicationRepository;
import com.revhire.revhirep2.repository.JobRepository;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.ResumeFileRepository;
import com.revhire.revhirep2.repository.ResumeRepository;
import com.revhire.revhirep2.service.ApplicationService;
import com.revhire.revhirep2.service.NotificationService;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeFileRepository resumeFileRepository;
    private final NotificationService notificationService;
    private static final Logger logger =
            LogManager.getLogger(ApplicationServiceImpl.class);
    
    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  JobRepository jobRepository,
                                  JobSeekerRepository jobSeekerRepository,
                                  ResumeRepository resumeRepository,
                                  ResumeFileRepository resumeFileRepository,
                                  NotificationService notificationService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.resumeRepository = resumeRepository;
        this.resumeFileRepository = resumeFileRepository;
        this.notificationService = notificationService;
    }

    // ================= APPLY =================
    @Override
    public void apply(ApplicationCreateDTO dto) {

        logger.info("Applying job: JobId={} JobSeekerId={}",
                dto.getJobId(), dto.getJobSeekerId());

        // check duplicate
        if (applicationRepository.existsByJobIdAndJobSeekerId(
                dto.getJobId(), dto.getJobSeekerId())) {

            throw new RuntimeException("Already applied to this job");
        }

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobSeeker jobSeeker = jobSeekerRepository.findById(dto.getJobSeekerId())
                .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

        Resume resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ResumeFile resumeFile = resumeFileRepository.findById(dto.getResumeFileId())
                .orElseThrow(() -> new RuntimeException("ResumeFile not found"));

        Application app = new Application();

        app.setJob(job);
        app.setJobSeeker(jobSeeker);
        app.setResume(resume);
        app.setResumeFile(resumeFile);
        app.setCoverLetter(dto.getCoverLetter());
        app.setStatus("APPLIED");

        applicationRepository.save(app);

        logger.info("Application saved successfully");

        notificationService.createNotification(
                job.getEmployer().getUser().getId(),
                "New Job Application",
                jobSeeker.getFullName() + " applied for your job '" + job.getTitle() + "'",
                "NEW_APPLICATION"
        );
    }

    // ================= WITHDRAW =================
    @Override
    public void withdraw(Long applicationId, Long jobSeekerId) {

    	
    	logger.info("JobSeeker {} attempting to withdraw application {}",
    	        jobSeekerId, applicationId);
    	
        Application app = applicationRepository
                .findByIdAndJobSeekerId(applicationId, jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if ("WITHDRAWN".equals(app.getStatus())) {
            throw new RuntimeException("Already withdrawn");
        }

        app.setStatus("WITHDRAWN");
        applicationRepository.save(app);
    }

    @Override
    public void bulkUpdateStatus(String employerEmail,
                                 List<Long> applicationIds,
                                 String status) {

        String normalizedStatus = status.toUpperCase();

        if (!isValidStatus(normalizedStatus)) {
            throw new RuntimeException("Invalid application status");
        }

        for (Long applicationId : applicationIds) {

            Application app = applicationRepository.findById(applicationId)
                    .orElseThrow(() ->
                            new RuntimeException("Application not found: " + applicationId));

            // 🔐 SECURITY CHECK
            if (!app.getJob()
                    .getEmployer()
                    .getUser()
                    .getEmail()
                    .equals(employerEmail)) {
                throw new RuntimeException("Unauthorized employer for application: " + applicationId);
            }

            // ❌ Skip withdrawn
            if ("WITHDRAWN".equalsIgnoreCase(app.getStatus())) {
                continue;
            }

            app.setStatus(normalizedStatus);
            applicationRepository.save(app);

            // 🔔 Notify Job Seeker
            notificationService.createNotification(
                    app.getJobSeeker().getUser().getId(),
                    "Application Status Updated",
                    "Your application for '" +
                            app.getJob().getTitle() +
                            "' is now " + normalizedStatus,
                    "APPLICATION_STATUS"
            );
        }
    }
    
    
    @Override
    public List<EmployerApplicationViewDTO> getAllApplicationsForEmployer(Long employerId) {

        return applicationRepository.findAll()
                .stream()
                .filter(app -> app.getJob().getEmployer().getId().equals(employerId))
                .map(app -> {

                    EmployerApplicationViewDTO dto = new EmployerApplicationViewDTO();

                    dto.setApplicationId(app.getId());
                    dto.setStatus(app.getStatus());
                    dto.setAppliedAt(app.getAppliedAt());

                    JobSeeker js = app.getJobSeeker();

                    dto.setFullName(js.getFullName());
                    dto.setEmail(js.getUser().getEmail());
                    dto.setPhone(js.getPhone());
                    dto.setLocation(js.getLocation());
                    dto.setEmploymentStatus(js.getEmploymentStatus());
                    dto.setProfileSummary(js.getProfileSummary());

                    dto.setResume(
                            ResumeMapper.toDTO(app.getResume())
                    );

                    dto.setResumeFile(
                            ResumeFileMapper.toDTO(app.getResumeFile())
                    );

                    dto.setCoverLetter(app.getCoverLetter());

                    dto.setJobTitle(app.getJob().getTitle());

                    return dto;

                }).toList();
    }
    
    
    // ================= JOB SEEKER VIEW =================
    @Override
    public List<ApplicationResponseDTO> getMyApplications(Long jobSeekerId) {

        return applicationRepository.findByJobSeekerId(jobSeekerId)
                .stream()
                .map(ApplicationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ================= EMPLOYER VIEW =================
    @Override
    public List<EmployerApplicationViewDTO> getApplicantsForJob(Long jobId, Long employerId) {

        Job job = jobRepository.findById(jobId).orElse(null);

        if (job == null) {
            return List.of();
        }

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(app -> {

                    EmployerApplicationViewDTO dto = new EmployerApplicationViewDTO();

                    dto.setApplicationId(app.getId());
                    dto.setStatus(app.getStatus());
                    dto.setAppliedAt(app.getAppliedAt());

                    // ✅ IMPORTANT FIX
                    dto.setJobTitle(app.getJob().getTitle());

                    JobSeeker js = app.getJobSeeker();

                    dto.setFullName(js.getFullName());
                    dto.setEmail(js.getUser().getEmail());
                    dto.setPhone(js.getPhone());
                    dto.setLocation(js.getLocation());
                    dto.setEmploymentStatus(js.getEmploymentStatus());
                    dto.setProfileSummary(js.getProfileSummary());

                    dto.setResume(
                            ResumeMapper.toDTO(app.getResume())
                    );

                    dto.setResumeFile(
                            ResumeFileMapper.toDTO(app.getResumeFile())
                    );

                    dto.setCoverLetter(app.getCoverLetter());

                    return dto;

                }).toList();
    }
    
    private boolean isValidStatus(String status) {

        return status.equals("APPLIED") ||
               status.equals("SHORTLISTED") ||
               status.equals("REJECTED") ||
               status.equals("INTERVIEW") ||
               status.equals("WITHDRAWN");
    }
    @Override
    public void updateStatus(Long applicationId, Long employerId, String status) {

    	logger.info("Employer {} attempting to update status of Application {} to {}",
    	        employerId, applicationId, status);
    	
    	Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // 🔐 SECURITY CHECK — ensure correct employer
        if (!app.getJob().getEmployer().getId().equals(employerId)) {
        	logger.error("Unauthorized employer {} tried to update application {}",
        	        employerId, applicationId);
            throw new RuntimeException("Unauthorized employer");
            
        }

        // 🔐 ROLE CHECK — ensure role is EMPLOYER
        if (!"EMPLOYER".equalsIgnoreCase(
                app.getJob().getEmployer().getUser().getRole())) {
            throw new RuntimeException("Only employers can update status");
        }

        // 🔒 STATUS VALIDATION
        String normalizedStatus = status.toUpperCase();

        if (!isValidStatus(normalizedStatus)) {
        	logger.warn("Invalid status '{}' provided for Application {}",
        	        status, applicationId);
            throw new RuntimeException("Invalid application status");
        }

        // ❌ Prevent updating withdrawn applications
        if ("WITHDRAWN".equalsIgnoreCase(app.getStatus())) {
            throw new RuntimeException("Cannot update withdrawn application");
        }

        app.setStatus(normalizedStatus);
        applicationRepository.save(app);
        logger.info("Application {} status updated to {} successfully",
                applicationId, normalizedStatus);

        // 🔔 Notify Job Seeker
        notificationService.createNotification(
                app.getJobSeeker().getUser().getId(),
                "Application Status Updated",
                "Your application for '" + app.getJob().getTitle() +
                        "' is now " + normalizedStatus,
                "APPLICATION_STATUS"
        );
    }
}