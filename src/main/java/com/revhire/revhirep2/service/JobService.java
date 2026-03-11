package com.revhire.revhirep2.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.revhire.revhirep2.dto.JobResponseDTO;
import com.revhire.revhirep2.entity.Job;

public interface JobService {

    Job createJob(Job job);

    Job updateJob(Long id, Job job);

    void deleteJob(Long id);

    Optional<Job> getById(Long id);

    List<Job> getByEmployerId(Long employerId);

    List<Job> searchByLocation(String location);

    List<Job> searchByExperience(Integer experience);

    List<Job> getAllActiveJobs();
    
    List<Job> searchByTitle(String title);

    List<Job> searchByCompanyName(String companyName);

    List<Job> searchBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary);

    List<Job> searchByJobType(String jobType);

    List<JobResponseDTO> advancedSearch(
            String title,
            String location,
            Integer experience,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String jobType,
            String companyName,
            LocalDateTime postedAfter
    );

    
    List<JobResponseDTO> getRecentJobsByEmployer(Long employerId);
    
    
	List<Job> searchByDatePosted(LocalDate datePosted);
}