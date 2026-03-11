package com.revhire.revhirep2.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revhire.revhirep2.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>{

    List<Job> findByEmployerId(Long employerId);

    List<Job> findByLocationContainingIgnoreCase(String location);

    List<Job> findByExperienceRequiredLessThanEqual(Integer experienceRequired);

    List<Job> findByStatus(String status);
    
    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByEmployer_CompanyNameContainingIgnoreCase(String companyName);

    List<Job> findBySalaryMinGreaterThanEqualAndSalaryMaxLessThanEqual(
            BigDecimal minSalary,
            BigDecimal maxSalary
    );

    List<Job> findByJobTypeIgnoreCase(String jobType);

    List<Job> findByCreatedAtAfter(LocalDateTime date);

    long countByEmployerId(Long employerId);

    List<Job> findTop5ByEmployerIdOrderByCreatedAtDesc(Long employerId);
    
    long countByEmployerIdAndStatus(Long employerId, String status);
}