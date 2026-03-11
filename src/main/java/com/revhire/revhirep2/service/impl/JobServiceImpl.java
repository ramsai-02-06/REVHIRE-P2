package com.revhire.revhirep2.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revhire.revhirep2.dto.JobResponseDTO;
import com.revhire.revhirep2.entity.Job;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.mapper.JobMapper;
import com.revhire.revhirep2.repository.ApplicationRepository;
import com.revhire.revhirep2.repository.EmployerRepository;
import com.revhire.revhirep2.repository.JobRepository;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.SavedJobRepository;
import com.revhire.revhirep2.service.JobService;
import com.revhire.revhirep2.service.NotificationService;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final NotificationService notificationService;
    private final JobSeekerRepository jobSeekerRepository;
    private final SavedJobRepository savedJobRepository;
    private final ApplicationRepository applicationRepository;

    public JobServiceImpl(JobRepository jobRepository,
            EmployerRepository employerRepository,
            JobSeekerRepository jobSeekerRepository,
            NotificationService notificationService,
            SavedJobRepository savedJobRepository,
            ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.notificationService = notificationService;
        this.savedJobRepository = savedJobRepository;
        this.applicationRepository = applicationRepository;
    }

    // ===============================
    // CREATE JOB
    // ===============================
    @Override
    public Job createJob(Job job) {

    	 job.setStatus("OPEN");
         job.setIsActive(true);
         job.setCreatedAt(LocalDateTime.now());

    	
    	Job savedJob = jobRepository.save(job);

        // 🔔 Send notifications to all Job Seekers
        List<JobSeeker> jobSeekers = jobSeekerRepository.findAll();

        for (JobSeeker seeker : jobSeekers) {

            notificationService.createNotification(
                    seeker.getUser().getId(),
                    "New Job Posted",
                    "A new job '" + savedJob.getTitle() + "' has been posted.",
                    "JOB_POSTED"
            );
        }
        return savedJob;
    }
    
   
    // ===============================
    // UPDATE JOB
    // ===============================
    @Override
    public Job updateJob(Long id, Job job) {

        Job existing = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        existing.setTitle(job.getTitle());
        existing.setCompanyName(job.getCompanyName());
        existing.setDescription(job.getDescription());
        existing.setSkillsRequired(job.getSkillsRequired());
        existing.setLocation(job.getLocation());
        existing.setSalaryMin(job.getSalaryMin());
        existing.setSalaryMax(job.getSalaryMax());
        existing.setExperienceRequired(job.getExperienceRequired());
        existing.setEducationRequired(job.getEducationRequired());
        existing.setJobType(job.getJobType());
        existing.setDeadline(job.getDeadline());
        existing.setOpenings(job.getOpenings());
        
        return jobRepository.save(existing);
    }

    // ===============================
    // DELETE JOB
    // — must remove child rows first to avoid FK constraint failures:
    //   saved_jobs.job_id  →  jobs.id
    //   applications.job_id →  jobs.id
    // ===============================
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteJob(Long id) {
        // 1. Remove all saved-job entries for this job
        savedJobRepository.findAll().stream()
                .filter(sj -> sj.getJob().getId().equals(id))
                .map(sj -> sj.getId())
                .forEach(savedJobRepository::deleteById);

        // 2. Remove all applications for this job
        applicationRepository.findByJobId(id)
                .stream()
                .map(app -> app.getId())
                .forEach(applicationRepository::deleteById);

        // 3. Now safe to delete the job itself
        jobRepository.deleteById(id);
    }

    // ===============================
    // GET BY ID
    // ===============================
    @Override
    public Optional<Job> getById(Long id) {
        return jobRepository.findById(id);
    }

    // ===============================
    // GET BY EMPLOYER
    // ===============================
    @Override
    public List<Job> getByEmployerId(Long employerId) {
        return jobRepository.findByEmployerId(employerId);
    }

    // ===============================
    // SEARCH BY LOCATION
    // ===============================
    @Override
    public List<Job> searchByLocation(String location) {
        return jobRepository.findByLocationContainingIgnoreCase(location);
    }

    // ===============================
    // SEARCH BY EXPERIENCE
    // ===============================
    @Override
    public List<Job> searchByExperience(Integer experience) {
        return jobRepository.findByExperienceRequiredLessThanEqual(experience);
    }

    // ===============================
    // SEARCH BY DATE POSTED (FULL DAY FIX)
    // ===============================
    @Override
    public List<Job> searchByDatePosted(LocalDate datePosted) {

        if (datePosted == null) {
            return jobRepository.findAll();
        }

        LocalDateTime startOfDay = datePosted.atStartOfDay();
        LocalDateTime endOfDay = datePosted.atTime(23, 59, 59);

        return jobRepository.findAll().stream()
                .filter(job ->
                        job.getCreatedAt() != null &&
                        !job.getCreatedAt().isBefore(startOfDay) &&
                        !job.getCreatedAt().isAfter(endOfDay)
                )
                .toList();
    }

    
    
    
    // ===============================
    // GET ALL ACTIVE JOBS
    // ===============================
    @Override
    public List<Job> getAllActiveJobs() {
        return jobRepository.findByStatus("OPEN");
    }

    @Override
    public List<Job> searchByTitle(String title) {
        return jobRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Job> searchByCompanyName(String companyName) {
        return jobRepository.findByEmployer_CompanyNameContainingIgnoreCase(companyName);
    }

    @Override
    public List<Job> searchBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return jobRepository
                .findBySalaryMinGreaterThanEqualAndSalaryMaxLessThanEqual(minSalary, maxSalary);
    }

    @Override
    public List<Job> searchByJobType(String jobType) {
        return jobRepository.findByJobTypeIgnoreCase(jobType);
    }
    
    

    @Override
    public List<JobResponseDTO> advancedSearch(
            String title,
            String location,
            Integer experience,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String jobType,
            String companyName,
            LocalDateTime postedAfter) {

        List<Job> jobs = jobRepository.findAll();

        if (title != null)
            jobs = jobs.stream()
                    .filter(j -> j.getTitle().toLowerCase()
                            .contains(title.toLowerCase()))
                    .toList();

        if (location != null)
            jobs = jobs.stream()
                    .filter(j -> j.getLocation().toLowerCase()
                            .contains(location.toLowerCase()))
                    .toList();

        if (experience != null)
            jobs = jobs.stream()
                    .filter(j -> j.getExperienceRequired() <= experience)
                    .toList();

        if (minSalary != null && maxSalary != null)
            jobs = jobs.stream()
                    .filter(j ->
                            j.getSalaryMin().compareTo(minSalary) >= 0 &&
                            j.getSalaryMax().compareTo(maxSalary) <= 0
                    ).toList();

        if (jobType != null)
            jobs = jobs.stream()
                    .filter(j -> j.getJobType()
                            .equalsIgnoreCase(jobType))
                    .toList();

        if (companyName != null)
            jobs = jobs.stream()
                    .filter(j -> j.getEmployer()
                            .getCompanyName()
                            .toLowerCase()
                            .contains(companyName.toLowerCase()))
                    .toList();

        if (postedAfter != null)
            jobs = jobs.stream()
                    .filter(j -> j.getCreatedAt()
                            .isAfter(postedAfter))
                    .toList();

        // 🔥 Convert to DTO
        return jobs.stream()
                .map(JobMapper::toDTO)
                .toList();
    }
    
    @Override
    public List<JobResponseDTO> getRecentJobsByEmployer(Long employerId) {
        return jobRepository
                .findTop5ByEmployerIdOrderByCreatedAtDesc(employerId)
                .stream()
                .map(JobMapper::toDTO)
                .toList();
    }
    
    
    
}