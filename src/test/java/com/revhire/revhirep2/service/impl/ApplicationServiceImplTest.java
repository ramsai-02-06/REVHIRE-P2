package com.revhire.revhirep2.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revhire.revhirep2.dto.ApplicationCreateDTO;
import com.revhire.revhirep2.entity.Application;
import com.revhire.revhirep2.entity.Employer;
import com.revhire.revhirep2.entity.Job;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.ResumeFile;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.repository.ApplicationRepository;
import com.revhire.revhirep2.repository.JobRepository;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.ResumeFileRepository;
import com.revhire.revhirep2.repository.ResumeRepository;
import com.revhire.revhirep2.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private ResumeFileRepository resumeFileRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private Job job;
    private JobSeeker jobSeeker;
    private Resume resume;
    private ResumeFile resumeFile;

    @BeforeEach
    void setup() {
        job = new Job();
        job.setId(1L);

        Employer employer = new Employer();
        User user = new User();
        user.setId(10L);
        employer.setUser(user);
        job.setEmployer(employer);

        jobSeeker = new JobSeeker();
        jobSeeker.setId(1L);

        resume = new Resume();
        resume.setId(1L);

        resumeFile = new ResumeFile();
        resumeFile.setId(1L);
    }

    @Test
    void apply_success() {

        ApplicationCreateDTO dto = new ApplicationCreateDTO();
        dto.setJobId(1L);
        dto.setJobSeekerId(1L);
        dto.setResumeId(1L);
        dto.setResumeFileId(1L);
        dto.setCoverLetter("Cover Letter");

        when(applicationRepository.existsByJobIdAndJobSeekerId(1L, 1L))
                .thenReturn(false);

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(job));

        when(jobSeekerRepository.findById(1L))
                .thenReturn(Optional.of(jobSeeker));

        when(resumeRepository.findById(1L))
                .thenReturn(Optional.of(resume));

        when(resumeFileRepository.findById(1L))
                .thenReturn(Optional.of(resumeFile));

        applicationService.apply(dto);

        verify(applicationRepository, times(1)).save(any(Application.class));
        verify(notificationService, times(1))
                .createNotification(any(), any(), any(), any());
    }

    @Test
    void apply_duplicate_shouldThrow() {

        ApplicationCreateDTO dto = new ApplicationCreateDTO();
        dto.setJobId(1L);
        dto.setJobSeekerId(1L);

        when(applicationRepository.existsByJobIdAndJobSeekerId(1L, 1L))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            applicationService.apply(dto);
        });
    }
    
    @Test
    void updateStatus_success() {

        Employer employer = new Employer();
        employer.setId(5L);

        User employerUser = new User();
        employerUser.setId(10L);
        employerUser.setRole("EMPLOYER");
        employer.setUser(employerUser);

        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle("Java Developer");

        JobSeeker jobSeeker = new JobSeeker();
        User jsUser = new User();
        jsUser.setId(20L);
        jobSeeker.setUser(jsUser);

        Application application = new Application();
        application.setId(1L);
        application.setStatus("APPLIED");
        application.setJob(job);
        application.setJobSeeker(jobSeeker);

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        applicationService.updateStatus(1L, 5L, "SHORTLISTED");

        verify(applicationRepository, times(1)).save(application);
        verify(notificationService, times(1))
                .createNotification(any(), any(), any(), any());
    }
    @Test
    void withdraw_alreadyWithdrawn_shouldThrow() {

        Application application = new Application();
        application.setId(1L);
        application.setStatus("WITHDRAWN");

        when(applicationRepository.findByIdAndJobSeekerId(1L, 1L))
                .thenReturn(Optional.of(application));

        assertThrows(RuntimeException.class, () -> {
            applicationService.withdraw(1L, 1L);
        });
    }
    
    @Test
    void withdraw_success() {

        Application application = new Application();
        application.setId(1L);
        application.setStatus("APPLIED");

        when(applicationRepository.findByIdAndJobSeekerId(1L, 1L))
                .thenReturn(Optional.of(application));

        applicationService.withdraw(1L, 1L);

        verify(applicationRepository, times(1)).save(application);
    }
    @Test
    void updateStatus_invalidEmployer_shouldThrow() {

        // Employer who owns the job
        Employer realEmployer = new Employer();
        realEmployer.setId(5L);

        User employerUser = new User();
        employerUser.setRole("EMPLOYER");
        realEmployer.setUser(employerUser);

        // Job
        Job job = new Job();
        job.setEmployer(realEmployer);
        job.setTitle("Java Developer");

        // Job Seeker
        JobSeeker jobSeeker = new JobSeeker();
        User jsUser = new User();
        jsUser.setId(20L);
        jobSeeker.setUser(jsUser);

        // Application
        Application application = new Application();
        application.setId(1L);
        application.setStatus("APPLIED");
        application.setJob(job);
        application.setJobSeeker(jobSeeker);

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        // ❌ Pass wrong employerId (not 5L)
        assertThrows(RuntimeException.class, () -> {
            applicationService.updateStatus(1L, 99L, "SHORTLISTED");
        });

        // Ensure save is never called
        verify(applicationRepository, times(0)).save(any());
    }
    
    @Test
    void updateStatus_withdrawnApplication_shouldThrow() {

        // Employer
        Employer employer = new Employer();
        employer.setId(5L);

        User employerUser = new User();
        employerUser.setRole("EMPLOYER");
        employer.setUser(employerUser);

        // Job
        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle("Java Developer");

        // Job Seeker
        JobSeeker jobSeeker = new JobSeeker();
        User jsUser = new User();
        jsUser.setId(20L);
        jobSeeker.setUser(jsUser);

        // Application already withdrawn
        Application application = new Application();
        application.setId(1L);
        application.setStatus("WITHDRAWN");
        application.setJob(job);
        application.setJobSeeker(jobSeeker);

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        // Try to update status
        assertThrows(RuntimeException.class, () -> {
            applicationService.updateStatus(1L, 5L, "SHORTLISTED");
        });

        // Ensure save is NOT called
        verify(applicationRepository, times(0)).save(any());
    }
    
    @Test
    void bulkUpdateStatus_success() {

        String employerEmail = "emp@test.com";

        User employerUser = new User();
        employerUser.setEmail(employerEmail);

        Employer employer = new Employer();
        employer.setUser(employerUser);

        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle("Java Developer");

        User jobSeekerUser = new User();
        jobSeekerUser.setId(100L);

        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setUser(jobSeekerUser);

        Application app1 = new Application();
        app1.setId(1L);
        app1.setJob(job);
        app1.setJobSeeker(jobSeeker);
        app1.setStatus("APPLIED");

        Application app2 = new Application();
        app2.setId(2L);
        app2.setJob(job);
        app2.setJobSeeker(jobSeeker);
        app2.setStatus("APPLIED");

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(app1));

        when(applicationRepository.findById(2L))
                .thenReturn(Optional.of(app2));

        applicationService.bulkUpdateStatus(
                employerEmail,
                List.of(1L, 2L),
                "SHORTLISTED"
        );

        verify(applicationRepository, times(2))
                .save(any(Application.class));

        verify(notificationService, times(2))
                .createNotification(anyLong(), anyString(), anyString(), anyString());
    }
    
    @Test
    void bulkUpdateStatus_invalidStatus_shouldThrow() {

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            applicationService.bulkUpdateStatus(
                    "emp@test.com",
                    List.of(1L),
                    "INVALID_STATUS"
            );
        });

        assertEquals("Invalid application status", ex.getMessage());
    }
    
    @Test
    void bulkUpdateStatus_unauthorizedEmployer_shouldThrow() {

        String employerEmail = "wrong@test.com";

        User employerUser = new User();
        employerUser.setEmail("correct@test.com");

        Employer employer = new Employer();
        employer.setUser(employerUser);

        Job job = new Job();
        job.setEmployer(employer);

        Application app = new Application();
        app.setId(1L);
        app.setJob(job);
        app.setStatus("APPLIED");

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(app));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            applicationService.bulkUpdateStatus(
                    employerEmail,
                    List.of(1L),
                    "SHORTLISTED"
            );
        });

        assertTrue(ex.getMessage().contains("Unauthorized"));
    }
    @Test
    void bulkUpdateStatus_withdrawn_shouldSkip() {

        String employerEmail = "emp@test.com";

        User employerUser = new User();
        employerUser.setEmail(employerEmail);

        Employer employer = new Employer();
        employer.setUser(employerUser);

        Job job = new Job();
        job.setEmployer(employer);

        Application withdrawnApp = new Application();
        withdrawnApp.setId(1L);
        withdrawnApp.setJob(job);
        withdrawnApp.setStatus("WITHDRAWN");

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(withdrawnApp));

        applicationService.bulkUpdateStatus(
                employerEmail,
                List.of(1L),
                "SHORTLISTED"
        );

        verify(applicationRepository, never())
                .save(withdrawnApp);

        verify(notificationService, never())
                .createNotification(anyLong(), anyString(), anyString(), anyString());
    }
}